package com.cm.payplaza.ecr_sdk_integration.uicomponents.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseEcrComponentViewModel<CS: BaseEcrComponentViewState>: ViewModel() {
    private val _state = MutableLiveData<CS>()
    val state : LiveData<CS> get() = _state

    fun updateView(vs: CS) {
        _state.value =  vs
    }

    abstract fun init()
}