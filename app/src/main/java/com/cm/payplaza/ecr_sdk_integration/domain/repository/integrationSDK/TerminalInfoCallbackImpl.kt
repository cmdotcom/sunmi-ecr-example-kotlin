package com.cm.payplaza.ecr_sdk_integration.domain.repository.integrationSDK

import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.entity.sdkEntity.SDKResponse
import com.cm.androidposintegration.service.callback.TerminalInfoCallback
import com.cm.androidposintegration.service.callback.beans.ErrorCode
import com.cm.androidposintegration.service.callback.beans.TerminalInfoData
import timber.log.Timber

class TerminalInfoCallbackImpl(
    private val localDataRepository: LocalDataRepository,
    private val integrationSDKCallback: IntegrationSDKManager.IntegrationSDKCallback
): TerminalInfoCallback {
    override fun onCrash() {
        integrationSDKCallback.returnResponse(SDKResponse.ON_CRASH)
    }

    override fun onError(error: ErrorCode) {
        integrationSDKCallback.returnResponse(SDKResponse.ON_ERROR)
    }

    override fun onResult(data: TerminalInfoData) {
        Timber.d("onResult - $data")
        val terminalData = TerminalData.toTerminalData(data)
        terminalData.currency?.let {
            localDataRepository.setTerminalData(terminalData)
            integrationSDKCallback.returnResponse(SDKResponse.ON_RESULT)
        } ?: run {
            integrationSDKCallback.returnResponse(SDKResponse.ON_ERROR)
        }
    }
}