package com.cm.payplaza.ecr_sdk_integration.fragment.receiptView

import com.cm.androidposintegration.enums.TransactionResult
import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.entity.Receipt
import com.cm.payplaza.ecr_sdk_integration.entity.TransactionResponse
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewModel
import com.cm.payplaza.ecr_sdk_integration.uicomponents.bottomAppBarComponent.BottomAppBarComponent
import com.cm.payplaza.ecr_sdk_integration.utils.printer.SunmiPrinter
import java.math.BigDecimal
import java.util.*

class ReceiptViewModel(
    private val localDataRepository: LocalDataRepository,
    private val printer: SunmiPrinter
) : BaseEcrFragmentViewModel<ReceiptState>() {
    override fun init() = printer.bindPrinter(::initView)

    fun checkReceipt() {
        val transactionResult = localDataRepository.getTransactionResponse()
        transactionResult?.let {
            val receiptMask = getReceiptMask(it)
            if (receiptMask != AvailableReceipts.NONE) {
                setUpSuccess(transactionResult, receiptMask)
            } else {
                updateView(ReceiptState.FinishTransaction)
            }
        } ?: run { updateView(ReceiptState.FinishTransaction) }
        localDataRepository.clearTransactionData()
    }

    private fun finishTransaction() {
        printer.unBindPrinter()
        updateView(ReceiptState.FinishTransaction)
    }

    fun merchantReceiptshowed(receipt: Receipt, isSuccesfull: Boolean) {
        updateView(ReceiptState.MerchantReceiptShowed(receipt, isSuccesfull))
    }

    fun printReceipt(receipt: Receipt) {
        printer.printReceipt(receipt)
    }

    fun setUpOneReceipt(receipt: Receipt) {
        val listener = object: BottomAppBarComponent.ClickListener {
            override fun onActionButtonPressed() {
                finishTransaction()
            }
            override fun onPrintButtonPressed() {
                printReceipt(receipt)
                finishTransaction()
            }
        }
        updateView(ReceiptState.SetUpBottomAppBar(listener))
    }

    fun setUpTwoReceipt(listener: BottomAppBarComponent.ClickListener) {
        updateView(ReceiptState.SetUpBottomAppBar(listener))
    }

    private fun setUpSuccess(
        transactionResponse: TransactionResponse,
        receiptMask: AvailableReceipts
    ) {
        val isSuccesfullTransaction = transactionResponse.result == TransactionResult.SUCCESS
        when (receiptMask) {
            AvailableReceipts.CUSTOMER -> {
                updateReceipt(transactionResponse.customerReceipt, null, isSuccesfullTransaction)
            }
            AvailableReceipts.MERCHANT -> {
                updateReceipt(transactionResponse.merchantReceipt, null, isSuccesfullTransaction)
            }
            AvailableReceipts.CUSTOMERANDMERCHANT -> {
                updateReceipt(
                    transactionResponse.customerReceipt,
                    transactionResponse.merchantReceipt,
                    isSuccesfullTransaction
                )
            }
            else -> updateView(ReceiptState.FinishTransaction)
        }

        if (transactionResponse.merchantReceipt != null) {
            updateReceipt(
                transactionResponse.customerReceipt,
                transactionResponse.merchantReceipt,
                isSuccesfullTransaction
            )
        } else {
            updateView(
                ReceiptState.SuccessOneReceipt(
                    transactionResponse.customerReceipt,
                    isSuccesfullTransaction
                )
            )
        }
    }

    private fun updateReceipt(receipt: Receipt?, merchantReceipt: Receipt?, result: Boolean) {
        if (receipt != null && merchantReceipt == null) {
            updateView(
                ReceiptState.SuccessOneReceipt(
                    receipt,
                    result
                )
            )
        } else if (receipt != null && merchantReceipt != null) {
            updateView(
                ReceiptState.SuccessTwoReceipt(
                    receipt,
                    merchantReceipt,
                    result
                )
            )
        } else {
            updateView(ReceiptState.FinishTransaction)
        }
    }

    private fun initView(isPrinterAvailable: Boolean) {
        updateView(
            ReceiptState.Init(
                isPrinterAvailable,
                localDataRepository.getTransactionResponse()?.transactionAmount ?: BigDecimal.ZERO,
                localDataRepository.getTransactionResponse()?.tippingAmount ?: BigDecimal.ZERO,
                localDataRepository.getTerminalData()?.currency ?: Currency.getInstance("EUR")
            )
        )
    }

    private fun getReceiptMask(transactionResult: TransactionResponse): AvailableReceipts {
        var receiptMask = 0
        receiptMask += if (!transactionResult.customerReceipt.isEmpty()) 1 else 0
        transactionResult.merchantReceipt?.let {
            receiptMask += if (!it.isEmpty()) 2 else 0
        }
        return AvailableReceipts.getByValue(receiptMask) ?: run { AvailableReceipts.NONE }
    }
}