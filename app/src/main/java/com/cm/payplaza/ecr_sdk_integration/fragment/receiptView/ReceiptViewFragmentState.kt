package com.cm.payplaza.ecr_sdk_integration.fragment.receiptView

import com.cm.payplaza.ecr_sdk_integration.entity.Receipt
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState

sealed class ReceiptViewFragmentState: BaseEcrFragmentViewState() {
    data class Init(val isPrinterAvailable: Boolean): ReceiptViewFragmentState()
    data class SuccessOneReceipt(val receipt: Receipt): ReceiptViewFragmentState()
    data class SuccessTwoReceipt(val customerReceipt: Receipt, val merchantReceipt: Receipt): ReceiptViewFragmentState()
    data class Canceled(val receipt: Receipt, val isPrinterAvailable: Boolean): ReceiptViewFragmentState()
    object ControlledTransactionError: ReceiptViewFragmentState()
    object FinishTransaction: ReceiptViewFragmentState()
    data class MerchantReceiptShowed(val receipt: Receipt): ReceiptViewFragmentState()
}