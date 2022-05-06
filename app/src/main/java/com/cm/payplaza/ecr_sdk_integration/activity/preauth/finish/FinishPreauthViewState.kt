package com.cm.payplaza.ecr_sdk_integration.activity.preauth.finish

import com.cm.payplaza.ecr_sdk_integration.activity.base.BaseEcrViewState
import com.cm.payplaza.ecr_sdk_integration.domain.repository.integrationSDK.IntegrationSDKManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class FinishPreauthViewState: BaseEcrViewState() {
    object OnResult: FinishPreauthViewState()
    object OnError: FinishPreauthViewState()
    object OnCrash: FinishPreauthViewState()
}