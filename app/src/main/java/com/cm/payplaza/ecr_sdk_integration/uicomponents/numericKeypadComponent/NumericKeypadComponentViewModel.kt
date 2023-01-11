package com.cm.payplaza.ecr_sdk_integration.uicomponents.numericKeypadComponent

import com.cm.payplaza.ecr_sdk_integration.uicomponents.base.BaseEcrComponentViewModel

class NumericKeypadComponentViewModel: BaseEcrComponentViewModel<NumericKeypadComponentState>() {
    override fun init() {
        updateView(NumericKeypadComponentState.Init)
    }

    fun enableBackspaceAndConfirm() {
        updateView(NumericKeypadComponentState.EnableBackSpaceAndConfirm)
    }

    fun disableBackspaceAndConfirm() {
        updateView(NumericKeypadComponentState.DisableBackSpaceAndConfirm)
    }

    fun disableNumericKeypad() {
        updateView(NumericKeypadComponentState.DisableNumericKeypad)
    }

    fun enableNumericKeypad() {
        updateView(NumericKeypadComponentState.EnableNumericKeypad)
    }

    fun clearKeypad() {
        updateView(NumericKeypadComponentState.ClearKeypad)
    }

    fun enableBackspace() {
        updateView(NumericKeypadComponentState.EnableBackspace)
    }
}