package com.cm.payplaza.ecr_sdk_integration.activity.refund

import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivityState

sealed class RefundViewState: BaseEcrFragmentActivityState() {
    object GoToTransactionActivity: RefundViewState()
    object GoToStanInsert: RefundViewState()
    object SkipStanInsert: RefundViewState()
    object GoToDateInsert: RefundViewState()
    object SkipDateInsert: RefundViewState()
}