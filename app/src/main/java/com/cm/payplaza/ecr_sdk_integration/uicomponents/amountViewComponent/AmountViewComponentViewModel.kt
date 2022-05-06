package com.cm.payplaza.ecr_sdk_integration.uicomponents.amountViewComponent

import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.uicomponents.base.BaseEcrComponentViewModel
import com.cm.payplaza.ecr_sdk_integration.utils.FormatUtils
import java.util.*

class AmountViewComponentViewModel(
        private val localDataRepository: LocalDataRepository
): BaseEcrComponentViewModel<AmountViewComponentViewState>() {
    private var _currencyExponent = 0

    override fun init() = updateView(AmountViewComponentViewState.Init)

    fun formatAmount(newInsertedDigits: Int) {
        val formattedAmount = FormatUtils.formatAmount(newInsertedDigits, _currencyExponent)
        updateView(AmountViewComponentViewState.UpdateInsertedDigits(formattedAmount))
    }

    fun addPinToken() = updateView(AmountViewComponentViewState.AddPinToken)

    fun clearView() = updateView(AmountViewComponentViewState.ClearView)

    fun addStanDigits(stanDigits: Int) = updateView(AmountViewComponentViewState.UpdateInsertedDigits(stanDigits.toString()))

    fun formatDate(date: Date) {
        val formattedDate = FormatUtils.formatDateForDateView(date)
        updateView(AmountViewComponentViewState.UpdateInsertedDigits(formattedDate))
    }

    fun setUpCurrency() {
        val terminalData = localDataRepository.getTerminalData()
        terminalData?.currency?.let {
            _currencyExponent = it.defaultFractionDigits
        }
        updateView(AmountViewComponentViewState.SetUpCurrency(localDataRepository.getTerminalData()))
    }
}