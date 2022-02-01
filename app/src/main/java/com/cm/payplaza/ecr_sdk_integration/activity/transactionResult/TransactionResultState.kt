package com.cm.payplaza.ecr_sdk_integration.activity.transactionResult

import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivityState

sealed class TransactionResultState: BaseEcrFragmentActivityState() {
    object GoToReceiptFragment: TransactionResultState()
    object OnResult: TransactionResultState()
    object OnCrash: TransactionResultState()
    object OnError: TransactionResultState()
    object OnFinishTransaction: TransactionResultState()
    object GoToStatuses: TransactionResultState()
}