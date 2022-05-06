package com.cm.payplaza.ecr_sdk_integration.activity.preauth.start

import com.cm.androidposintegration.enums.TransactionType
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivityViewModel
import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.entity.Transaction
import org.koin.core.component.inject
import timber.log.Timber
import java.util.*

class PreAuthViewModel: BaseEcrFragmentActivityViewModel() {

    private val localDataRepository: LocalDataRepository by inject()

    fun savePreauth(insertedDigits: Int) {
        Timber.d("savePreauth")
        val amount = (insertedDigits.toDouble() / 100).toBigDecimal()
        val merchantData = localDataRepository.getTerminalData()
        val currency = merchantData?.currency ?: run {
            Currency.getInstance(Locale.getDefault())
        }
        val transactionData = Transaction(amount, currency, TransactionType.PRE_AUTH, refundDate = null, refundStan = null)
        localDataRepository.setTransaction(transactionData)
        updateView(PreAuthViewState.GoToTransactionResult)
    }
}