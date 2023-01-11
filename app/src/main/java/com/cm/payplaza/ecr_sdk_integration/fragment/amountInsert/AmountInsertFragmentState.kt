package com.cm.payplaza.ecr_sdk_integration.fragment.amountInsert

import android.view.View
import com.cm.payplaza.ecr_sdk_integration.databinding.ComponentBottomAppBarBinding
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import com.cm.payplaza.ecr_sdk_integration.uicomponents.bottomAppBarComponent.BottomAppBarComponent

sealed class AmountInsertFragmentState: BaseEcrFragmentViewState() {
    data class Init(val data: TerminalData?): AmountInsertFragmentState()
    object ClearAmount: AmountInsertFragmentState()
    data class NextStep(val amount: Int): AmountInsertFragmentState()
    data class AddDigitToAmountView(val newInsertedDigits: Int): AmountInsertFragmentState()
    data class SetupBottomAppBar(val listener: BottomAppBarComponent.ClickListener): AmountInsertFragmentState()
    object EnableNextButton: AmountInsertFragmentState()
}