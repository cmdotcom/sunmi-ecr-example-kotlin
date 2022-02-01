package com.cm.payplaza.ecr_sdk_integration.fragment.amountInsert

import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState

sealed class AmountInsertFragmentState: BaseEcrFragmentViewState() {
    data class Init(val data: TerminalData?): AmountInsertFragmentState()
    object ClearAmount: AmountInsertFragmentState()
    data class NextStep(val amount: Int): AmountInsertFragmentState()
    data class AddDigitToAmountView(val newInsertedDigits: Int): AmountInsertFragmentState()
}