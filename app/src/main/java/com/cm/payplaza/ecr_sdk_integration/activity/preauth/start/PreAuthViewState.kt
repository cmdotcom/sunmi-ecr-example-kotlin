package com.cm.payplaza.ecr_sdk_integration.activity.preauth.start

import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivityState

sealed class PreAuthViewState: BaseEcrFragmentActivityState() {
    object GoToTransactionResult: PreAuthViewState()
    object EnableAutoTimezone: PreAuthViewState()
}