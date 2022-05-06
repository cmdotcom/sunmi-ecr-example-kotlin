package com.cm.payplaza.ecr_sdk_integration.fragment.error

import com.cm.androidposintegration.service.callback.beans.ErrorCode
import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.entity.TransactionError
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewModel

class ErrorFragmentViewModel(private val localDataRepository: LocalDataRepository):
    BaseEcrFragmentViewModel<ErrorFragmentState>() {
    override fun init() {
        val error = localDataRepository.getTransactionError()
        error?.let { checkError(it) }
        ?: run { dismiss() }
    }

    fun dismiss() = updateView(ErrorFragmentState.Dismiss)

    private fun checkError(transactionError: TransactionError) {
        when(ErrorCode.getByValue(transactionError.value)) {
            ErrorCode.AUTO_TIMEZONE_NOT_ENABLED -> updateView(ErrorFragmentState.AutoTimezoneNotEnabled)
            ErrorCode.LOW_BATTERY_LEVEL -> updateView(ErrorFragmentState.LowBatteryLevel)
            else -> { dismiss() }
        }
    }
}