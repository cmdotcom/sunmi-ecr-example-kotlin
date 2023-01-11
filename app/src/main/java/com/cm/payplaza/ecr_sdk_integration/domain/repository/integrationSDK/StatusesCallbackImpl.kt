package com.cm.payplaza.ecr_sdk_integration.domain.repository.integrationSDK

import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.entity.SDKError
import com.cm.payplaza.ecr_sdk_integration.entity.StatusesData
import com.cm.payplaza.ecr_sdk_integration.entity.TransactionError
import com.cm.payplaza.ecr_sdk_integration.entity.sdkEntity.SDKResponse
import com.cm.androidposintegration.service.callback.StatusesCallback
import com.cm.androidposintegration.service.callback.beans.ErrorCode
import com.cm.androidposintegration.service.callback.beans.TransactionStatusesData
import com.cm.payplaza.ecr_sdk_integration.R

class StatusesCallbackImpl(
    private val localDataRepository: LocalDataRepository,
    private val integrationSDKCallback: IntegrationSDKManager.IntegrationSDKCallback
): StatusesCallback {
    override fun onCrash() {
        val errorStr = SDKError.map.getOrDefault(-1, R.string.error_occurred)
        val transactionError = TransactionError(errorStr, -1)
        localDataRepository.clearTransactionData()
        localDataRepository.setTransactionError(transactionError)
        integrationSDKCallback.returnResponse(SDKResponse.ON_CRASH)
    }

    override fun onError(error: ErrorCode) {
        val errorStr = SDKError.map.getOrDefault(error.value, R.string.error_occurred)
        val transactionError = TransactionError(errorStr, error.value)
        localDataRepository.clearTransactionData()
        localDataRepository.setTransactionError(transactionError)
        integrationSDKCallback.returnResponse(SDKResponse.ON_ERROR)
    }

    override fun onResult(data: TransactionStatusesData) {
        val statusesDataEntity = StatusesData.toStatusesData(data)
        localDataRepository.setStatusesData(statusesDataEntity)
        integrationSDKCallback.returnResponse(SDKResponse.ON_RESULT)
    }
}