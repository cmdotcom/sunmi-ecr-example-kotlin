package com.cm.payplaza.ecr_sdk_integration.uicomponents.amountViewComponent

import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.uicomponents.base.BaseEcrComponentViewState

sealed class AmountViewComponentViewState: BaseEcrComponentViewState() {
    object Init: AmountViewComponentViewState()
    data class UpdateInsertedDigits(val newInsertedDigits: String): AmountViewComponentViewState()
    object AddPinToken: AmountViewComponentViewState()
    object ClearView: AmountViewComponentViewState()
    data class SetUpCurrency(val merchantData: TerminalData?): AmountViewComponentViewState()
}