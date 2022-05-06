package com.cm.payplaza.ecr_sdk_integration.fragment.receiptView

import com.cm.androidposintegration.enums.TransactionResult
import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.entity.Receipt
import com.cm.payplaza.ecr_sdk_integration.entity.TransactionResponse
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewModel
import com.cm.payplaza.ecr_sdk_integration.utils.printer.SunmiPrinter

class ReceiptViewFragmentViewModel(
    private val localDataRepository: LocalDataRepository,
    private val printer: SunmiPrinter
): BaseEcrFragmentViewModel<ReceiptViewFragmentState>() {
    override fun init() = printer.bindPrinter(::initView)

    fun checkReceipt() {
        val transactionResult = localDataRepository.getTransactionResponse()
        transactionResult?.let {
            val receiptMask = getReceiptMask(it)
            if(receiptMask != AvailableReceipts.NONE) {
                setUpSuccess(transactionResult, receiptMask)
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

    fun merchantReceiptshowed(receipt: Receipt, isSuccesfull: Boolean) {
        updateView(ReceiptViewFragmentState.MerchantReceiptShowed(receipt, isSuccesfull))
    }

    fun printReceipt(receipt: Receipt) {
        printer.printReceipt(receipt)
    }

    private fun setUpSuccess(transactionResponse: TransactionResponse, receiptMask: AvailableReceipts) {
        val isSuccesfullTransaction = transactionResponse.result == TransactionResult.SUCCESS
        when(receiptMask) {
            AvailableReceipts.CUSTOMER -> {
                updateView(ReceiptViewFragmentState.SuccessOneReceipt(
                    transactionResponse.customerReceipt,
                    isSuccesfullTransaction))
            }
            AvailableReceipts.MERCHANT -> {
                transactionResponse.merchantReceipt?.let{
                    updateView(ReceiptViewFragmentState.SuccessOneReceipt(
                        receipt = it,
                        isSuccesfullTransaction))
                } ?: run {
                    updateView(ReceiptViewFragmentState.FinishTransaction)
                }
            }
            AvailableReceipts.CUSTOMERANDMERCHANT -> {
                transactionResponse.merchantReceipt?.let{
                    updateView(ReceiptViewFragmentState.SuccessTwoReceipt(
                        transactionResponse.customerReceipt,
                        merchantReceipt = it,
                        isSuccesfullTransaction))
                } ?: run {
                    updateView(ReceiptViewFragmentState.FinishTransaction)
                }
            }
            else -> { updateView(ReceiptViewFragmentState.FinishTransaction) }
        }
        transactionResponse.merchantReceipt?.let {
            updateView(ReceiptViewFragmentState.SuccessTwoReceipt(
                transactionResponse.customerReceipt,
                transactionResponse.merchantReceipt,
                isSuccesfullTransaction))
        } ?: run { updateView(ReceiptViewFragmentState.SuccessOneReceipt(
            transactionResponse.customerReceipt,
            isSuccesfullTransaction))
        }
    }

    private fun initView(isPrinterAvailable: Boolean) {
        updateView(ReceiptViewFragmentState.Init(isPrinterAvailable))
    }

    private fun getReceiptMask(transactionResult: TransactionResponse): AvailableReceipts {
        var receiptMask = 0
        receiptMask += if(!transactionResult.customerReceipt.isEmpty()) 1 else 0
        transactionResult.merchantReceipt?.let {
            receiptMask += if(!it.isEmpty()) 2 else 0
        }
        return AvailableReceipts.getByValue(receiptMask) ?: run { AvailableReceipts.NONE }
    }
}