package com.cm.payplaza.ecr_sdk_integration.activity.transactionResult

import com.cm.androidposintegration.enums.TransactionType
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivityState

sealed class TransactionResultState: BaseEcrFragmentActivityState() {
    object GoToReceiptFragment: TransactionResultState()
    object OnResult: TransactionResultState()
    object OnCrash: TransactionResultState()
    object OnError: TransactionResultState()
    object OnFinishTransaction: TransactionResultState()
    object GoToStatuses: TransactionResultState()
    data class SetTransactionType(val transactionType: TransactionType): TransactionResultState()
    object ShowWarningPopupPrinterOutOfPaper: TransactionResultState()
    data class RestoreBottomAppBar(val isPrinterOutOfPaper: Boolean): TransactionResultState()
}