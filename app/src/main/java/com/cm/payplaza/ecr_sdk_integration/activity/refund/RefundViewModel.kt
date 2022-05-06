package com.cm.payplaza.ecr_sdk_integration.activity.refund

import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivityViewModel
import com.cm.payplaza.ecr_sdk_integration.entity.Transaction
import com.cm.androidposintegration.enums.TransactionType
import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import org.koin.core.component.inject
import timber.log.Timber
import java.util.*

class RefundViewModel: BaseEcrFragmentActivityViewModel() {

    private val localDataRepository: LocalDataRepository by inject()

    private var _refundAmount = 0
    private var _refundStan: String? = null
    private var _date: Date? = null

    fun saveAmount(amount: Int) {
        Timber.d("saveAmount: $amount")
        _refundAmount = amount
    }

    fun saveStan(stan: String) {
        Timber.d("saveStan: $stan")
        _refundStan = stan
    }

    fun saveDate(date: Date) {
        Timber.d("saveDate: $date")
        _date = date
    }

    fun prepareRefund() {
        Timber.d("prepareRefund")
        val amount = (_refundAmount.toDouble() / 100).toBigDecimal()
        val merchantData = localDataRepository.getTerminalData()
        val currency = merchantData?.currency ?: run {
            Currency.getInstance(Locale.getDefault())
        }
        val transactionData = Transaction(amount, currency, TransactionType.REFUND, _date, _refundStan)
        Timber.d("prepareRefund:\n  - date $_date\n" +
                "  - amount $_refundAmount\n" +
                "  - currency ${currency.symbol}\n" +
                "  - type REFUND\n" +
                "  - Stan $_refundStan")
        localDataRepository.setTransaction(transactionData)
        updateView(RefundViewState.GoToTransactionActivity)
    }

    fun checkForStanInsert() {
        val terminalData = localDataRepository.getTerminalData()
        if(terminalData != null && "NL" == terminalData.storeCountry) {
            updateView(RefundViewState.SkipStanInsert)
        } else {
            updateView(RefundViewState.GoToStanInsert)
        }
    }
}