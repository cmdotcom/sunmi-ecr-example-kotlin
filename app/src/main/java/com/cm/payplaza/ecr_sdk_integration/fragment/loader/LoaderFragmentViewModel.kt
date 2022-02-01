package com.cm.payplaza.ecr_sdk_integration.fragment.loader

import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewModel
import timber.log.Timber

class LoaderFragmentViewModel(
    private val localDataRepository: LocalDataRepository
): BaseEcrFragmentViewModel<LoaderFragmentState>() {
    override fun init() {
        val transactionData = localDataRepository.getTransaction()
        transactionData?.let {
            updateView(LoaderFragmentState.Init(
                    it.amount,
                    it.currency.symbol,
                    it.transactionType))
            Timber.d("LoaderFragmentState.Init\n - amount ${it.amount}\n" +
                    " - currency ${it.currency.symbol}\n" +
                    " - type ${it.transactionType}")
        } ?: run { updateView(LoaderFragmentState.HideFooter) }
    }
}