package com.cm.payplaza.ecr_sdk_integration.fragment.amountInsert

import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewModel

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
}