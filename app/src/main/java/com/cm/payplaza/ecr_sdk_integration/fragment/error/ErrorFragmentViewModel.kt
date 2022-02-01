package com.cm.payplaza.ecr_sdk_integration.fragment.error

import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewModel

class ErrorFragmentViewModel(private val localDataRepository: LocalDataRepository):
    BaseEcrFragmentViewModel<ErrorFragmentState>() {
    override fun init() {
        val error = localDataRepository.getTransactionError()
        error?.let { updateView(ErrorFragmentState.Init(error))
        } ?: run { updateView(ErrorFragmentState.Dismiss) }
    }

    fun dismiss() = updateView(ErrorFragmentState.Dismiss)
}