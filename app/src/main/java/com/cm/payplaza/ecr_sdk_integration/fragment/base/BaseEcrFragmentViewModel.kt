package com.cm.payplaza.ecr_sdk_integration.fragment.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseEcrFragmentViewModel<FVS: BaseEcrFragmentViewState>: ViewModel() {
    private val _state = MutableLiveData<FVS>()
    val state : LiveData<FVS> get() = _state

    protected fun updateView(vs: FVS) {
        _state.value =  vs
    }

    abstract fun init()
}