package com.cm.payplaza.ecr_sdk_integration.domain.repository.integrationSDK

import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.entity.Receipt
import com.cm.payplaza.ecr_sdk_integration.entity.SDKError
import com.cm.payplaza.ecr_sdk_integration.entity.TransactionError
import com.cm.payplaza.ecr_sdk_integration.entity.TransactionResponse
import com.cm.payplaza.ecr_sdk_integration.entity.sdkEntity.SDKResponse
import com.cm.androidposintegration.enums.TransactionResult
import com.cm.androidposintegration.service.callback.ReceiptCallback
import com.cm.androidposintegration.service.callback.beans.ErrorCode
import com.cm.androidposintegration.service.callback.beans.LastReceiptResultData

class TotalsCallbackImpl(
    private val localDataRepository: LocalDataRepository,
    private val integrationSDKCallback: IntegrationSDKManager.IntegrationSDKCallback
): ReceiptCallback {
    override fun onCrash() {
        val errorStr = SDKError.map.getOrDefault(-1, "Error")
        val transactionError = TransactionError(errorStr, -1)
        localDataRepository.clearTransactionData()
        localDataRepository.setTransactionError(transactionError)
        integrationSDKCallback.returnResponse(SDKResponse.ON_CRASH)
    }

    override fun onError(error: ErrorCode) {
        val errorStr = SDKError.map.getOrDefault(error.value, "Error")
        val transactionError = TransactionError(errorStr, -1)
        localDataRepository.clearTransactionData()
        localDataRepository.setTransactionError(transactionError)
        integrationSDKCallback.returnResponse(SDKResponse.ON_ERROR)
    }

    override fun onResult(data: LastReceiptResultData) {
        data.receiptData?.let {
            val receipt = Receipt(
                it.receiptLines,
                it.signature)
            val transactionResponse = TransactionResponse(
                TransactionResult.SUCCESS,
                "",
                receipt,
                null)
            localDataRepository.increaseOrderReference()
            localDataRepository.clearTransactionData()
            localDataRepository.setTransactionResponse(transactionResponse)
            integrationSDKCallback.returnResponse(SDKResponse.ON_RESULT)
        } ?: run { integrationSDKCallback.returnResponse(SDKResponse.ON_CRASH) }
    }
}