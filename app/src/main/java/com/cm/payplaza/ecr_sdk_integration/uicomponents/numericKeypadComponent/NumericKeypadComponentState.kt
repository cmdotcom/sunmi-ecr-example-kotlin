package com.cm.payplaza.ecr_sdk_integration.uicomponents.numericKeypadComponent

import com.cm.payplaza.ecr_sdk_integration.uicomponents.base.BaseEcrComponentViewState

sealed class NumericKeypadComponentState: BaseEcrComponentViewState() {
    object Init : NumericKeypadComponentState()
    object EnableBackSpaceAndConfirm: NumericKeypadComponentState()
    object DisableBackSpaceAndConfirm: NumericKeypadComponentState()
    object DisableNumericKeypad: NumericKeypadComponentState()
    object EnableNumericKeypad: NumericKeypadComponentState()
    object ClearKeypad: NumericKeypadComponentState()
    object EnableBackspace: NumericKeypadComponentState()
    object EnableContinue: NumericKeypadComponentState()
}