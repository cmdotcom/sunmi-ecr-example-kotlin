package com.cm.payplaza.ecr_sdk_integration.activity.lastReceipt

import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivityState

open class LastReceiptState: BaseEcrFragmentActivityState() {
    object OnResult: LastReceiptState()
    object OnCrash: LastReceiptState()
    object OnError: LastReceiptState()
}