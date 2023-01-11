package com.cm.payplaza.ecr_sdk_integration.activity.totals

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.activity.base.BaseEcrViewState
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivity
import com.cm.payplaza.ecr_sdk_integration.activity.payment.PaymentActivity
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import com.cm.payplaza.ecr_sdk_integration.fragment.error.ErrorFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.receiptView.ReceiptState
import com.cm.payplaza.ecr_sdk_integration.uicomponents.bottomAppBarComponent.BottomAppBarComponent
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class TotalsActivity : BaseEcrFragmentActivity<TotalsViewModel>() {

    companion object {
        fun start(context: Context) {
            Timber.d("goToTotals")
            val intent = Intent(context, TotalsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

    override val viewModel: TotalsViewModel by viewModel()

    override fun render(state: BaseEcrViewState) {
        super.render(state)
        when (state) {
            TotalsState.OnCrash -> goToErrorFragment()
            TotalsState.OnError -> goToErrorFragment()
            TotalsState.OnResult -> goToReceiptFragment()
        }
    }

    override fun renderFragment(state: BaseEcrFragmentViewState) {
        when (state) {
            ErrorFragmentState.Dismiss -> PaymentActivity.start(this)
            is ReceiptState.ControlledTransactionError -> goToErrorFragment()
            ReceiptState.FinishTransaction -> PaymentActivity.start(this)
            is ReceiptState.SetUpBottomAppBar -> setupBottomAppBar(state.listener)
            is ReceiptState.Init -> setUpPrinterButton(state.isPrinterAvailable)
            is ErrorFragmentState.SetUpBottomAppBar -> setUpBottomBarForError(state.listener)
        }
    }

    override fun setUpMenu() {
        super.setUpMenu()
        setMenuStatuses(
            listOf(
                Pair(getString(R.string.cancel_payment), false),
                Pair(getString(R.string.day_totals), false)
            )
        )
    }

    override fun initializeView(terminalData: TerminalData?) {
        super.initializeView(terminalData)
        if (isActivityRestored) {
            restoreActivity()
        } else {
            viewModel.getTotals()
        }
    }

    override fun getNavigationGraph(): Int {
        return R.navigation.totals_graph
    }

    override fun restoreActivity() {
        viewModel.error()
        goToErrorFragment()
    }

    private fun setUpPrinterButton(printerAvailable: Boolean) {
        binding.bottomAppView.setupPrinterButtonVisibility(printerAvailable)
    }

    private fun setUpBottomBarForError(listener: BottomAppBarComponent.ClickListener) {
        binding.bottomAppView.enableActionButton()
        binding.bottomAppView.setActionButtonText(R.string.bottom_app_bar_card_payment_continue)
        binding.bottomAppView.setTransactionTypeText(R.string.bottom_app_bar_day_totals)
        binding.bottomAppView.setButtonsListeners(listener)
    }

    private fun setupBottomAppBar(listener: BottomAppBarComponent.ClickListener) {
        binding.bottomAppView.setActionButtonText(R.string.bottom_button)
        binding.bottomAppView.enableActionButton()
        binding.bottomAppView.setPrintButtonText(R.string.print_receipt)
        binding.bottomAppView.setButtonsListeners(listener)
        binding.bottomAppView.setIconsForPrint()
    }

    private fun goToReceiptFragment() {
        binding.progressLoader.visibility = View.GONE
        navController.navigate(
            R.id.action_loaderFragment2_to_receiptViewFragment2,
            Bundle().apply { putBoolean("dayTotals", true) })
    }

    private fun goToErrorFragment() {
        binding.progressLoader.visibility = View.GONE
        navController.navigate(R.id.action_loaderFragment2_to_receiptViewFragment2)
    }
}