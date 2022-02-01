package com.cm.payplaza.ecr_sdk_integration.fragment.base

import timber.log.Timber

open class BaseEcrFragmentViewState {
    init { Timber.d(this.javaClass.superclass.simpleName + " -> " + this.javaClass.simpleName) }
}