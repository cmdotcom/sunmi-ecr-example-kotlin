package com.cm.payplaza.ecr_sdk_integration.activity.lastReceipt

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivityViewModel
import com.cm.payplaza.ecr_sdk_integration.domain.repository.integrationSDK.IntegrationSDKManager
import com.cm.payplaza.ecr_sdk_integration.entity.sdkEntity.SDKResponse
import com.cm.androidposintegration.beans.LastReceiptOptions
import com.cm.payplaza.ecr_sdk_integration.activity.base.BaseEcrViewState
import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import timber.log.Timber

class LastReceiptViewModel: BaseEcrFragmentActivityViewModel() {

    private val integrationSDKManager: IntegrationSDKManager by inject()

    fun getLastReceipt() {
        Timber.d("getLastReceipt")
        val callback = object : (IntegrationSDKManager.IntegrationSDKCallback) {
            override fun returnResponse(sdkResponse: SDKResponse) {
                when(sdkResponse) {
                    SDKResponse.ON_RESULT -> updateView(LastReceiptState.OnResult)
                    SDKResponse.ON_ERROR -> updateView(LastReceiptState.OnError)
                    SDKResponse.ON_CRASH -> updateView(LastReceiptState.OnCrash)
                }
            }
        }
        val options = LastReceiptOptions(false)
        viewModelScope.launch {
            integrationSDKManager.doLastReceipt(options, callback)
        }
    }
}