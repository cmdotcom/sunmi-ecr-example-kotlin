package com.cm.payplaza.ecr_sdk_integration.fragment.error

import com.cm.payplaza.ecr_sdk_integration.entity.TransactionError
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState

sealed class ErrorFragmentState: BaseEcrFragmentViewState() {
    object Dismiss: ErrorFragmentState()
    object AutoTimezoneNotEnabled: ErrorFragmentState()
    object LowBatteryLevel: ErrorFragmentState()
}