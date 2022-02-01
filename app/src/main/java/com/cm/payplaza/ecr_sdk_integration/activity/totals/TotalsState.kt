package com.cm.payplaza.ecr_sdk_integration.activity.totals

import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivityState

sealed class TotalsState: BaseEcrFragmentActivityState() {
    object OnCrash: TotalsState()
    object OnError: TotalsState()
    object OnResult: TotalsState()
}