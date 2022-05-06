package com.cm.payplaza.ecr_sdk_integration.fragment.loader

import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import com.cm.androidposintegration.enums.TransactionType
import java.math.BigDecimal

sealed class LoaderFragmentState: BaseEcrFragmentViewState() {
    data class Init(val amount: BigDecimal, val currency: String, val type: TransactionType): LoaderFragmentState()
    object HideFooter: LoaderFragmentState()
}