package com.cm.payplaza.ecr_sdk_integration.fragment.receiptView

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.entity.Receipt
import com.cm.payplaza.ecr_sdk_integration.entity.TransactionResponse
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewModel
import com.cm.payplaza.ecr_sdk_integration.utils.printer.SunmiPrinter
import timber.log.Timber

class ReceiptViewFragmentViewModel(
    private val localDataRepository: LocalDataRepository,
    private val printer: SunmiPrinter
): BaseEcrFragmentViewModel<ReceiptViewFragmentState>() {
    override fun init() = printer.bindPrinter(::initView)

    fun checkReceipt() {
        val transactionResult = localDataRepository.getTransactionResponse()
        transactionResult?.let {
            if(!transactionResult.customerReceipt.isEmpty()) {
                setUpSuccess(transactionResult)
            } else {
                updateView(ReceiptViewFragmentState.FinishTransaction)
            }
        } ?: run { updateView(ReceiptViewFragmentState.FinishTransaction) }
        localDataRepository.clearTransactionData()
    }

    fun finishTransaction() {
        printer.unBindPrinter()
        updateView(ReceiptViewFragmentState.FinishTransaction)
    }

    fun merchantReceiptshowed(receipt: Receipt) {
        updateView(ReceiptViewFragmentState.MerchantReceiptShowed(receipt))
    }

    fun printReceipt(receipt: Receipt) {
        printer.printReceipt(receipt)
    }

    private fun setUpSuccess(transactionResult: TransactionResponse) {
        transactionResult.merchantReceipt?.let {
            updateView(ReceiptViewFragmentState.SuccessTwoReceipt(
                transactionResult.customerReceipt,
                transactionResult.merchantReceipt))
        } ?: run { updateView(ReceiptViewFragmentState.SuccessOneReceipt(transactionResult.customerReceipt)) }
    }

    private fun setUpCanceled(transactionResult: TransactionResponse) {
        transactionResult.customerReceipt.receiptLines?.let {
            val isPrinterAvailable = printer.isPrinterAvailable()
            updateView(ReceiptViewFragmentState.Canceled(transactionResult.customerReceipt, isPrinterAvailable))
        } ?: run { updateView(ReceiptViewFragmentState.FinishTransaction) }
    }

    private fun initView(isPrinterAvailable: Boolean) {
        updateView(ReceiptViewFragmentState.Init(isPrinterAvailable))
    }
}