package com.cm.payplaza.ecr_sdk_integration.activity.totals

import androidx.lifecycle.viewModelScope
import com.cm.androidposintegration.beans.DayTotalsOptions
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivityViewModel
import com.cm.payplaza.ecr_sdk_integration.domain.repository.integrationSDK.IntegrationSDKManager
import com.cm.payplaza.ecr_sdk_integration.entity.sdkEntity.SDKResponse
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import timber.log.Timber

class TotalsViewModel: BaseEcrFragmentActivityViewModel() {

    private val integrationSDKManager: IntegrationSDKManager by inject()

    fun getTotals() {
        Timber.d("getTotals")
        val options = DayTotalsOptions(false)
        val callback = object : IntegrationSDKManager.IntegrationSDKCallback {
            override fun returnResponse(sdkResponse: SDKResponse) {
                when(sdkResponse) {
                    SDKResponse.ON_RESULT -> updateView(TotalsState.OnResult)
                    SDKResponse.ON_ERROR -> updateView(TotalsState.OnError)
                    SDKResponse.ON_CRASH -> updateView(TotalsState.OnCrash)
                }
            }
        }
        viewModelScope.launch {
            integrationSDKManager.doTotals(options, callback)
        }
    }
}