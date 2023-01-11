package com.cm.payplaza.ecr_sdk_integration.fragment.statuses

import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.entity.StatusData
import com.cm.payplaza.ecr_sdk_integration.entity.TransactionResponse
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewModel
import java.math.BigDecimal
import com.cm.payplaza.ecr_sdk_integration.uicomponents.bottomAppBarComponent.BottomAppBarComponent

class StatusesFragmentViewModel(
    private val localDataRepository: LocalDataRepository
) : BaseEcrFragmentViewModel<StatusesFragmentState>() {
    override fun init() {
        val statusesData = localDataRepository.getStatusesData()
        statusesData?.let {
            if (statusesData.count > 0) {
                updateView(StatusesFragmentState.Init(it))
            } else {
                updateView(StatusesFragmentState.NoDataAvailable)
            }
        } ?: run { updateView(StatusesFragmentState.NoDataAvailable) }
    }

    fun lastReceipt() = updateView(StatusesFragmentState.GoToLastReceipt)
    fun continueToNewPayment() = updateView(StatusesFragmentState.GoToPayment)
    fun showReceipt(status: StatusData) {
        val transactionResponse = TransactionResponse(
            status.result,
            (localDataRepository.getOrderReference() - 1).toString(),
            status.receipt,
            null,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
        )
        localDataRepository.setTransactionResponse(transactionResponse)
        updateView(StatusesFragmentState.GoToReceiptView)
    }
    fun setupBottomAppBar() {
        val listener = object: BottomAppBarComponent.ClickListener {
            override fun onActionButtonPressed() {
                continueToNewPayment()
            }

            override fun onPrintButtonPressed() {}
        }
        updateView(StatusesFragmentState.SetupBottomAppBar(listener))
    }
}