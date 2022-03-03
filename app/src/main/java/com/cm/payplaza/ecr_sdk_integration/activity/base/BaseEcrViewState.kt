package com.cm.payplaza.ecr_sdk_integration.activity.base

import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import timber.log.Timber

open class BaseEcrViewState: Any() {
    init { Timber.d(this.javaClass.superclass.simpleName + " -> " + this.javaClass.simpleName) }
    data class Init(val terminalData: TerminalData?): BaseEcrViewState()
    data class RequestInfo(val terminalData: TerminalData?): BaseEcrViewState()
    object RequestInfoFailed: BaseEcrViewState()
    object RequestInfoLoader: BaseEcrViewState()
}