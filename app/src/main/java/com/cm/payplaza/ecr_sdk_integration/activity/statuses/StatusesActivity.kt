package com.cm.payplaza.ecr_sdk_integration.activity.statuses

import android.content.Context
import android.content.Intent
import androidx.core.view.GravityCompat
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.activity.base.BaseEcrViewState
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivity
import com.cm.payplaza.ecr_sdk_integration.activity.lastReceipt.LastReceiptActivity
import com.cm.payplaza.ecr_sdk_integration.activity.payment.PaymentActivity
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import com.cm.payplaza.ecr_sdk_integration.fragment.error.ErrorFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.receiptView.ReceiptViewFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.statuses.StatusesFragmentState
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class StatusesActivity: BaseEcrFragmentActivity<StatusesViewModel>() {

    companion object {
        fun start(context: Context) {
            Timber.d("goToStatusesFragment")
            val intent = Intent(context, StatusesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

    override val viewModel: StatusesViewModel by viewModel()

    override fun render(state: BaseEcrViewState) {
        super.render(state)
        when(state) {
            StatusesState.OnResult -> goToStatusesFragment()
            StatusesState.OnCrash -> goToErrorFragment()
            StatusesState.OnError -> goToErrorFragment()
        }
    }

    override fun renderFragment(state: BaseEcrFragmentViewState) {
        when(state) {
            StatusesFragmentState.NoDataAvailable -> goToErrorFromStatuses()
            StatusesFragmentState.GoToReceiptView -> goToReceiptView()
            StatusesFragmentState.GoToLastReceipt -> LastReceiptActivity.start(this)
            StatusesFragmentState.GoToPayment -> PaymentActivity.start(this)
            ErrorFragmentState.Dismiss -> PaymentActivity.start(this)
            ReceiptViewFragmentState.FinishTransaction -> goToStatusesFromReceipt()
            ReceiptViewFragmentState.ControlledTransactionError -> goToStatusesFromReceipt()
            else -> { }
        }
    }

    override fun setUpMenu() {
        super.setUpMenu()
        binding.ecrNavigation.setNavigationItemSelectedListener {
            binding.ecrDrawer.closeDrawer(GravityCompat.START)
            false
        }
        val menu = binding.ecrNavigation.menu
        menu.findItem(R.id.nav_cancel_payment).isEnabled = false
        menu.findItem(R.id.nav_print_last_receipt).isEnabled = false
        menu.findItem(R.id.nav_day_totals).isEnabled = false
        menu.findItem(R.id.nav_new_payment).isEnabled = false
        menu.findItem(R.id.nav_refund).isEnabled = false
    }

    override fun initializeView(terminalData: TerminalData?) {
        super.initializeView(terminalData)
        viewModel.getStatuses()
    }

    override fun setUpBookmark() {
        Timber.d("setUpBookmark")
        binding.ecrBookmarkBar.setSelectedBookmark(5, R.string.statuses)
    }

    override fun getNavigationGraph(): Int {
        return R.navigation.statuses_graph
    }

    private fun goToErrorFragment() {
        Timber.d("goToErrorFragment")
        navController.navigate(R.id.action_loaderFragment4_to_errorFragment3)
    }

    private fun goToErrorFromStatuses() {
        Timber.d("goToErrorFragment")
        navController.navigate(R.id.action_statusesFragment_to_errorFragment3)
    }

    private fun goToStatusesFragment() {
        Timber.d("goToStatusesFragment")
        navController.navigate(R.id.action_loaderFragment4_to_statusesFragment)
    }

    private fun goToReceiptView() {
        Timber.d("goToReceiptView")
        navController.navigate(R.id.action_statusesFragment_to_receiptViewFragment3)
    }

    private fun goToStatusesFromReceipt() {
        Timber.d("goToStatusesFromReceipt")
        navController.navigate(R.id.action_receiptViewFragment3_to_statusesFragment4)
    }
}