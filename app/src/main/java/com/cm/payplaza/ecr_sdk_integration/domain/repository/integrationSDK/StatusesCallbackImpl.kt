package com.cm.payplaza.ecr_sdk_integration.domain.repository.integrationSDK

import com.cm.androidposintegration.service.callback.StatusesCallback
import com.cm.androidposintegration.service.callback.beans.ErrorCode
import com.cm.androidposintegration.service.callback.beans.TransactionStatusesData
import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.entity.SDKError
import com.cm.payplaza.ecr_sdk_integration.entity.StatusesData
import com.cm.payplaza.ecr_sdk_integration.entity.TransactionError
import com.cm.payplaza.ecr_sdk_integration.entity.sdkEntity.SDKResponse

class StatusesCallbackImpl(
    private val localDataRepository: LocalDataRepository,
    private val integrationSDKCallback: IntegrationSDKManager.IntegrationSDKCallback
): StatusesCallback {
    override fun onCrash() {
        val errorStr = SDKError.map.getOrDefault(-1, "Crash")
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
        if(ErrorCode.getByValue(error.value) == ErrorCode.AUTO_TIMEZONE_NOT_ENABLED) {
            localDataRepository.setTimezoneEnabled(false)
        }
        integrationSDKCallback.returnResponse(SDKResponse.ON_ERROR)
    }

    override fun onResult(data: TransactionStatusesData) {
        val statusesDataEntity = StatusesData.toStatusesData(data)
        localDataRepository.setStatusesData(statusesDataEntity)
        integrationSDKCallback.returnResponse(SDKResponse.ON_RESULT)
    }
}