package com.cm.payplaza.ecr_sdk_integration.activity.payment

import com.cm.androidposintegration.enums.TransactionType
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivityViewModel
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.entity.Transaction
import timber.log.Timber
import java.util.*

class PaymentViewModel: BaseEcrFragmentActivityViewModel() {

    fun savePayment(insertedDigits: Int) {
        Timber.d("savePayment")
        val amount = (insertedDigits.toDouble() / 100).toBigDecimal()
        val merchantData = localDataRepository.getTerminalData()
        val currency = merchantData?.currency ?: run {
            Currency.getInstance(Locale.getDefault())
        }
        val transactionData = Transaction(amount, currency, TransactionType.PURCHASE, null, null)
        localDataRepository.setTransaction(transactionData)
        updateView(PaymentViewState.GoToTransactionResult)
    }
}