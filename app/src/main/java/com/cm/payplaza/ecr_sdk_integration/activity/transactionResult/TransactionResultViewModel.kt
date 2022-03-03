package com.cm.payplaza.ecr_sdk_integration.activity.transactionResult

import androidx.lifecycle.viewModelScope
import com.cm.androidposintegration.beans.TransactionData
import com.cm.androidposintegration.enums.TransactionResult
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivityViewModel
import com.cm.payplaza.ecr_sdk_integration.domain.repository.integrationSDK.IntegrationSDKManager
import com.cm.payplaza.ecr_sdk_integration.entity.sdkEntity.SDKResponse
import kotlinx.coroutines.launch
import java.util.*

class TransactionResultViewModel: BaseEcrFragmentActivityViewModel() {

    fun doTransaction() {
        if (localDataRepository.getTransaction() != null) {
            val transactionData = localDataRepository.getTransaction()
            val orderReference = localDataRepository.getOrderReference()
            if (transactionData != null) {
                val newTransactionData = TransactionData(
                    transactionData.transactionType,
                    transactionData.amount,
                    transactionData.currency,
                    orderReference.toString()
                )
                transactionData.refundDate?.let {
                    transactionData.refundStan?.let {
                        newTransactionData.refundDate = transactionData.refundDate
                        newTransactionData.refundStan = transactionData.refundStan
                        newTransactionData.isCaptureSignature = true
                    }
                }
                newTransactionData.isShowReceipt = false
                newTransactionData.language = Locale.getDefault().language
                val callback = object : (IntegrationSDKManager.IntegrationSDKCallback) {
                    override fun returnResponse(sdkResponse: SDKResponse) {
                        when (sdkResponse) {
                            SDKResponse.ON_RESULT -> updateView(TransactionResultState.OnResult)
                            SDKResponse.ON_ERROR -> updateView(TransactionResultState.OnFinishTransaction)
                            SDKResponse.ON_CRASH -> updateView(TransactionResultState.GoToStatuses)
                        }
                    }
                }
                viewModelScope.launch {
                    integrationSDKManager.doTransaction(newTransactionData, callback)
                }
            } else {
                updateView(TransactionResultState.OnCrash)
            }
        } else {
            updateView(TransactionResultState.OnCrash)
        }
    }
}
