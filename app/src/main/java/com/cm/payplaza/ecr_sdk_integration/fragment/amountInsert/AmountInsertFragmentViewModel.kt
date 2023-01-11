package com.cm.payplaza.ecr_sdk_integration.fragment.amountInsert

import android.view.View
import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewModel
import com.cm.payplaza.ecr_sdk_integration.uicomponents.bottomAppBarComponent.BottomAppBarComponent

class AmountInsertFragmentViewModel(
    private val localDataRepository: LocalDataRepository
): BaseEcrFragmentViewModel<AmountInsertFragmentState>() {
    private var _insertedDigits: Int = 0
    override fun init() {
        val terminalData = localDataRepository.getTerminalData()
        updateView(AmountInsertFragmentState.Init(terminalData))
    }

    fun addDigitToAmountView(digit: Int) {
        _insertedDigits = (_insertedDigits * 10) + digit
        if(_insertedDigits <= 0) {
            _insertedDigits = 0
            updateView(AmountInsertFragmentState.ClearAmount)
        } else {
            updateView(AmountInsertFragmentState.AddDigitToAmountView(_insertedDigits))
        }
    }

    fun clearAmount() {
        _insertedDigits = 0
        updateView(AmountInsertFragmentState.ClearAmount)
    }

    fun doPayment() {
        updateView(AmountInsertFragmentState.NextStep(_insertedDigits))
    }

    fun setupBottomAppBar() {
        val listener = object: BottomAppBarComponent.ClickListener {
            override fun onActionButtonPressed() {
                doPayment()
            }

            override fun onPrintButtonPressed() {}

        }
        updateView(AmountInsertFragmentState.SetupBottomAppBar(listener))
    }

    fun enableNextButton() {
        updateView(AmountInsertFragmentState.EnableNextButton)
    }
}