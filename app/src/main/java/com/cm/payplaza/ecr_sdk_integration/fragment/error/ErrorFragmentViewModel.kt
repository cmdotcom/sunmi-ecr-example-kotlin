package com.cm.payplaza.ecr_sdk_integration.fragment.error

import com.cm.androidposintegration.service.callback.beans.ErrorCode
import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.entity.TransactionError
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewModel
import com.cm.payplaza.ecr_sdk_integration.uicomponents.bottomAppBarComponent.BottomAppBarComponent
import timber.log.Timber

class ErrorFragmentViewModel(private val localDataRepository: LocalDataRepository):
    BaseEcrFragmentViewModel<ErrorFragmentState>() {
    override fun init() {
        val error = localDataRepository.getTransactionError()
        error?.let { checkError(it) }
        ?: run { dismiss() }
    }

    fun dismiss() = updateView(ErrorFragmentState.Dismiss)

    fun setupBottomAppBar() {
        val listener = object: BottomAppBarComponent.ClickListener {
            override fun onActionButtonPressed() {
                dismiss()
            }
            override fun onPrintButtonPressed() {}
        }
        updateView(ErrorFragmentState.SetUpBottomAppBar(listener))
    }

    private fun checkError(transactionError: TransactionError) {
        transactionError.transactionMessage?.let {
            updateView(ErrorFragmentState.TransactionError(it))
        } ?: run {
            when(ErrorCode.getByValue(transactionError.value)) {
                ErrorCode.AUTO_TIMEZONE_NOT_ENABLED -> updateView(ErrorFragmentState.AutoTimezoneNotEnabled(transactionError.stringId))
                ErrorCode.LOW_BATTERY_LEVEL -> updateView(ErrorFragmentState.LowBatteryLevel(transactionError.stringId))
                ErrorCode.BAD_TIMEZONE -> updateView(ErrorFragmentState.BadTimezone(transactionError.stringId))
                ErrorCode.NO_ERROR -> { dismiss() }
                else -> { updateView(ErrorFragmentState.Error(transactionError.stringId)) }
            }
        }
    }
}