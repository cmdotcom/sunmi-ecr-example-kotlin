package com.cm.payplaza.ecr_sdk_integration.activity.payment

import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivityState

sealed class PaymentViewState: BaseEcrFragmentActivityState() {
    object GoToTransactionResult: PaymentViewState()
    object EnableAutoTimezone: PaymentViewState()
}