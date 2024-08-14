package com.cm.payplaza.ecr_sdk_integration.activity.payment

import com.cm.androidposintegration.enums.TransactionType
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivityViewModel
import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.entity.Transaction
import org.koin.core.component.inject
import java.util.Currency
import java.util.Locale
import kotlin.math.pow

class PaymentViewModel: BaseEcrFragmentActivityViewModel() {

    private val localDataRepository: LocalDataRepository by inject()

    fun savePayment(insertedDigits: Int) {
        var doubleAmount = insertedDigits.toDouble()
        val merchantData = localDataRepository.getTerminalData()
        val currency = merchantData?.currency ?: run {
            Currency.getInstance(Locale.getDefault())
        }
        doubleAmount /= 10.0.pow(currency.defaultFractionDigits.toDouble())
        val amount = doubleAmount.toBigDecimal()
        val transactionData = Transaction(amount, currency, TransactionType.PURCHASE, null, null)
        localDataRepository.setTransaction(transactionData)
        updateView(PaymentViewState.GoToTransactionResult)
    }
}