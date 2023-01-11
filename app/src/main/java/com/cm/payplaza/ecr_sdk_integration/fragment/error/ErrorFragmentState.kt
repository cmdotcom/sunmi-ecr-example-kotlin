package com.cm.payplaza.ecr_sdk_integration.fragment.error

import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import com.cm.payplaza.ecr_sdk_integration.uicomponents.bottomAppBarComponent.BottomAppBarComponent

sealed class ErrorFragmentState: BaseEcrFragmentViewState() {
    object Dismiss: ErrorFragmentState()
    data class AutoTimezoneNotEnabled(val messageId: Int): ErrorFragmentState()
    data class LowBatteryLevel(val messageId: Int): ErrorFragmentState()
    data class BadTimezone(val messageId: Int): ErrorFragmentState()
    data class Error(val messageId: Int): ErrorFragmentState()
    data class TransactionError(val message: String): ErrorFragmentState()
    data class SetUpBottomAppBar(val listener: BottomAppBarComponent.ClickListener): ErrorFragmentState()
}