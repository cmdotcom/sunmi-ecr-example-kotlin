package com.cm.payplaza.ecr_sdk_integration.domain.repository.integrationSDK

import com.cm.androidposintegration.enums.TransactionResult
import com.cm.androidposintegration.service.callback.TransactionCallback
import com.cm.androidposintegration.service.callback.beans.ErrorCode
import com.cm.androidposintegration.service.callback.beans.TransactionResultData
import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.entity.Receipt
import com.cm.payplaza.ecr_sdk_integration.entity.SDKError
import com.cm.payplaza.ecr_sdk_integration.entity.TransactionError
import com.cm.payplaza.ecr_sdk_integration.entity.TransactionResponse
import com.cm.payplaza.ecr_sdk_integration.entity.sdkEntity.SDKResponse
import timber.log.Timber
import java.math.BigDecimal

class DoTransactionCallbackImpl(
    private val localDataRepository: LocalDataRepository,
    private val integrationSDKCallback: IntegrationSDKManager.IntegrationSDKCallback):
    TransactionCallback {
    override fun onCrash() {
        val orderReference = localDataRepository.getOrderReference().toString()
        val transactionResponse = TransactionResponse(
            TransactionResult.REQUEST_RECEIPT,
            orderReference,
            Receipt.empty(),
            Receipt.empty())
        Timber.d("TransactionResponse - $transactionResponse")
        localDataRepository.increaseOrderReference()
        localDataRepository.clearTransactionData()
        localDataRepository.setTransactionResponse(transactionResponse)
        integrationSDKCallback.returnResponse(SDKResponse.ON_CRASH)
    }

    override fun onError(error: ErrorCode) {
        val transactionData = localDataRepository.getTransaction()
        transactionData?.let {
            val errorStr = SDKError.map.getOrDefault(error.value, "Error")
            val transactionError = TransactionError(errorStr, transactionData.amount)
            localDataRepository.clearTransactionData()
            localDataRepository.setTransactionError(transactionError)
            Timber.e("onError - $error")
        }
        integrationSDKCallback.returnResponse(SDKResponse.ON_ERROR)
    }

    override fun onResult(data: TransactionResultData) {
        when(data.transactionResult) {
            TransactionResult.SUCCESS,
            TransactionResult.CANCELED -> setUpOkTransactionResult(data)
            TransactionResult.REQUEST_RECEIPT -> setUpRequestReceiptResult(data)
            else -> setUpTransactionError(data)
        }
    }

    private fun setUpOkTransactionResult(data: TransactionResultData) {
        setUpTransactionResult(data)
        integrationSDKCallback.returnResponse(SDKResponse.ON_RESULT)
    }

    private fun setUpRequestReceiptResult(data: TransactionResultData) {
        setUpTransactionResult(data)
        integrationSDKCallback.returnResponse(SDKResponse.ON_CRASH)
    }

    private fun setUpTransactionError(data: TransactionResultData) {
        setUpErrorResult(data)
        integrationSDKCallback.returnResponse(SDKResponse.ON_ERROR)
    }

    private fun setUpTransactionResult(data: TransactionResultData) {
        val customerReceipt = Receipt(
            data.customerReceipt?.receiptLines,
            data.customerReceipt?.signature)
        var merchantReceipt: Receipt? = null
        data.merchantReceipt?.let {
            merchantReceipt = Receipt(
                it.receiptLines,
                it.signature)
        }
        val transactionResponse = TransactionResponse(
            data.transactionResult,
            data.orderReference,
            customerReceipt,
            merchantReceipt)
        Timber.d("TransactionResponse - $transactionResponse")
        localDataRepository.increaseOrderReference()
        localDataRepository.clearTransactionData()
        localDataRepository.setTransactionResponse(transactionResponse)
    }

    private fun setUpErrorResult(data: TransactionResultData) {
        val desc = data.transactionResult.description
        val amount = data.amount ?: run { BigDecimal(0) }
        val transactionError = TransactionError(desc, amount)
        Timber.d("TransactionError - $desc")
        localDataRepository.clearTransactionData()
        localDataRepository.setTransactionError(transactionError)
    }
}