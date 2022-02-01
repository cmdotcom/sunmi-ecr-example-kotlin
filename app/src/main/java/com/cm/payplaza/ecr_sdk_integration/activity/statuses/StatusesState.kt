package com.cm.payplaza.ecr_sdk_integration.activity.statuses

import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivityState

open class StatusesState: BaseEcrFragmentActivityState() {
    object OnResult: StatusesState()
    object OnError: StatusesState()
    object OnCrash: StatusesState()
}