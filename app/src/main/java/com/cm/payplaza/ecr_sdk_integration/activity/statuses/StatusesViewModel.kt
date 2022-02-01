package com.cm.payplaza.ecr_sdk_integration.activity.statuses

import androidx.lifecycle.viewModelScope
import com.cm.androidposintegration.beans.RequestStatusData
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivityViewModel
import com.cm.payplaza.ecr_sdk_integration.domain.repository.integrationSDK.IntegrationSDKManager
import com.cm.payplaza.ecr_sdk_integration.entity.sdkEntity.SDKResponse
import kotlinx.coroutines.launch
import timber.log.Timber

class StatusesViewModel: BaseEcrFragmentActivityViewModel() {

    fun getStatuses() {
        Timber.d("getStatuses")
        val callback = object: IntegrationSDKManager.IntegrationSDKCallback {
            override fun returnResponse(sdkResponse: SDKResponse) {
                when(sdkResponse) {
                    SDKResponse.ON_RESULT -> updateView(StatusesState.OnResult)
                    SDKResponse.ON_ERROR -> updateView(StatusesState.OnError)
                    SDKResponse.ON_CRASH -> updateView(StatusesState.OnCrash)
                }
            }
        }
        val orderReference = localDataRepository.getOrderReference() - 1
        val request = RequestStatusData(orderReference.toString())
         viewModelScope.launch {
             integrationSDKManager.doStatuses(request, callback)
        }
    }
}