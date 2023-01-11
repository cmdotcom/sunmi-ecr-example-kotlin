package com.cm.payplaza.ecr_sdk_integration.fragment.receiptView

import com.cm.payplaza.ecr_sdk_integration.entity.Receipt
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import com.cm.payplaza.ecr_sdk_integration.uicomponents.bottomAppBarComponent.BottomAppBarComponent
import java.math.BigDecimal
import java.util.Currency

sealed class ReceiptState : BaseEcrFragmentViewState() {
    data class Init(
        val isPrinterAvailable: Boolean,
        val transactionAmount: BigDecimal,
        val tippingAmount: BigDecimal,
        val currency: Currency,
    ) : ReceiptState()

    data class SuccessOneReceipt(val receipt: Receipt, val isSuccessful: Boolean) : ReceiptState()
    data class SuccessTwoReceipt(
        val customerReceipt: Receipt,
        val merchantReceipt: Receipt,
        val isSuccessful: Boolean
    ) : ReceiptState()

    data class MerchantReceiptShowed(val receipt: Receipt, val isSuccessful: Boolean) :
        ReceiptState()

    data class Canceled(val receipt: Receipt, val isPrinterAvailable: Boolean) : ReceiptState()
    data class SetUpBottomAppBar(val listener: BottomAppBarComponent.ClickListener) : ReceiptState()
    object ControlledTransactionError : ReceiptState()
    object FinishTransaction : ReceiptState()
}