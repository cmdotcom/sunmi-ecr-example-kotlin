package com.cm.payplaza.ecr_sdk_integration.koin

import com.cm.payplaza.ecr_sdk_integration.activity.lastReceipt.LastReceiptViewModel
import com.cm.payplaza.ecr_sdk_integration.activity.payment.PaymentViewModel
import com.cm.payplaza.ecr_sdk_integration.activity.preauth.finish.FinishPreauthViewModel
import com.cm.payplaza.ecr_sdk_integration.activity.preauth.start.PreAuthViewModel
import com.cm.payplaza.ecr_sdk_integration.activity.refund.RefundViewModel
import com.cm.payplaza.ecr_sdk_integration.activity.statuses.StatusesViewModel
import com.cm.payplaza.ecr_sdk_integration.activity.totals.TotalsViewModel
import com.cm.payplaza.ecr_sdk_integration.activity.transactionResult.TransactionResultViewModel
import com.cm.payplaza.ecr_sdk_integration.fragment.amountInsert.AmountInsertFragmentViewModel
import com.cm.payplaza.ecr_sdk_integration.fragment.dateInsert.DateInsertFragmentViewModel
import com.cm.payplaza.ecr_sdk_integration.fragment.error.ErrorFragmentViewModel
import com.cm.payplaza.ecr_sdk_integration.fragment.loader.LoaderFragmentViewModel
import com.cm.payplaza.ecr_sdk_integration.fragment.passwordInsert.PasswordInsertFragmentViewModel
import com.cm.payplaza.ecr_sdk_integration.fragment.receiptView.ReceiptViewModel
import com.cm.payplaza.ecr_sdk_integration.fragment.stanInsert.StanInsertViewModel
import com.cm.payplaza.ecr_sdk_integration.fragment.statuses.StatusesFragmentViewModel
import com.cm.payplaza.ecr_sdk_integration.uicomponents.amountViewComponent.AmountViewComponentViewModel
import com.cm.payplaza.ecr_sdk_integration.uicomponents.numericKeypadComponent.NumericKeypadComponentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    // Activities view model
    viewModel { PaymentViewModel() }
    viewModel { TransactionResultViewModel() }
    viewModel { RefundViewModel() }
    viewModel { LastReceiptViewModel() }
    viewModel { TotalsViewModel() }
    viewModel { StatusesViewModel() }
    viewModel { PreAuthViewModel() }
    viewModel { FinishPreauthViewModel() }

    // Component view model
    viewModel { AmountViewComponentViewModel(get()) }
    viewModel { NumericKeypadComponentViewModel() }

    // Fragment view model
    viewModel { ReceiptViewModel(get(), get()) }
    viewModel { LoaderFragmentViewModel(get()) }
    viewModel { AmountInsertFragmentViewModel(get()) }
    viewModel { ErrorFragmentViewModel(get()) }
    viewModel { PasswordInsertFragmentViewModel(get()) }
    viewModel { StanInsertViewModel(get()) }
    viewModel { DateInsertFragmentViewModel(get()) }
    viewModel { StatusesFragmentViewModel(get()) }
}