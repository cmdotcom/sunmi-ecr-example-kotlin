package com.cm.payplaza.ecr_sdk_integration.uicomponents.base

import timber.log.Timber

abstract class BaseEcrComponentViewState{
    init { Timber.d(this.javaClass.superclass.simpleName + " -> " + this.javaClass.simpleName) }
}