package com.cm.payplaza.ecr_sdk_integration.activity.totals

import android.content.Context
import android.content.Intent
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.activity.base.BaseEcrViewState
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivity
import com.cm.payplaza.ecr_sdk_integration.activity.payment.PaymentActivity
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import com.cm.payplaza.ecr_sdk_integration.fragment.error.ErrorFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.receiptView.ReceiptViewFragmentState
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class TotalsActivity: BaseEcrFragmentActivity<TotalsViewModel>() {

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
        when(state) {
            TotalsState.OnCrash -> goToErrorFragment()
            TotalsState.OnError -> goToErrorFragment()
            TotalsState.OnResult -> goToReceiptFragment()
        }
    }

    override fun renderFragment(state: BaseEcrFragmentViewState) {
        when(state) {
            ErrorFragmentState.Dismiss -> PaymentActivity.start(this)
            is ReceiptViewFragmentState.ControlledTransactionError -> goToErrorFragment()
            ReceiptViewFragmentState.FinishTransaction -> PaymentActivity.start(this)
        }
    }

    override fun setUpMenu() {
        super.setUpMenu()
        val menu = binding.ecrNavigation.menu
        menu.findItem(R.id.nav_cancel_payment).isEnabled = false
        menu.findItem(R.id.nav_day_totals).isEnabled = false
    }

    override fun initializeView(terminalData: TerminalData?) {
        super.initializeView(terminalData)
        viewModel.getTotals()
    }

    override fun setUpBookmark() {
        Timber.d("setUpBookmark")
        binding.ecrBookmarkBar.setSelectedBookmark(5, R.string.bookmark_totals)
    }

    override fun getNavigationGraph(): Int {
        return R.navigation.totals_graph
    }

    private fun goToReceiptFragment() {
        Timber.d("goToReceiptFragment")
        navController.navigate(R.id.action_loaderFragment2_to_receiptViewFragment2)
    }

    private fun goToErrorFragment() {
        Timber.d("goToErrorFragment")
        navController.navigate(R.id.action_loaderFragment2_to_receiptViewFragment2)
    }
}