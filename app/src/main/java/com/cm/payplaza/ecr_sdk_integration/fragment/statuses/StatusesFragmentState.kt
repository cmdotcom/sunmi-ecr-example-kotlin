package com.cm.payplaza.ecr_sdk_integration.fragment.statuses

import com.cm.payplaza.ecr_sdk_integration.entity.StatusesData
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState

open class StatusesFragmentState: BaseEcrFragmentViewState() {
    data class Init(val data: StatusesData): StatusesFragmentState()
    object NoDataAvailable: StatusesFragmentState()
    object GoToReceiptView: StatusesFragmentState()
    object GoToLastReceipt: StatusesFragmentState()
    object GoToPayment: StatusesFragmentState()
}