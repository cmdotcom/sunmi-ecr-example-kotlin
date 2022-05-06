package com.cm.payplaza.ecr_sdk_integration.activity.lastReceipt

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

class LastReceiptActivity: BaseEcrFragmentActivity<LastReceiptViewModel>() {

    companion object {
        fun start(context: Context) {
            Timber.d("goToLastReceipt")
            val intent = Intent(context, LastReceiptActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

    override val viewModel: LastReceiptViewModel by viewModel()

    override fun render(state: BaseEcrViewState) {
        super.render(state)
        when(state) {
            LastReceiptState.OnCrash -> goToErrorFragment()
            LastReceiptState.OnError -> goToErrorFragment()
            LastReceiptState.OnResult -> goToReceiptFragment()
        }
    }

    override fun renderFragment(state: BaseEcrFragmentViewState) {
        when(state) {
            ErrorFragmentState.Dismiss -> PaymentActivity.start(this)
            ReceiptViewFragmentState.ControlledTransactionError -> goToErrorFragment()
            ReceiptViewFragmentState.FinishTransaction -> PaymentActivity.start(this)
        }
    }

    override fun setUpMenu() {
        super.setUpMenu()
        val menu = binding.ecrNavigation.menu
        menu.findItem(R.id.nav_cancel_payment).isEnabled = false
        menu.findItem(R.id.nav_print_last_receipt).isEnabled = false
    }

    override fun initializeView(terminalData: TerminalData?) {
        super.initializeView(terminalData)
        viewModel.getLastReceipt()
    }

    override fun setUpBookmark() {
        Timber.d("setUpBookmark")
        binding.ecrBookmarkBar.setSelectedBookmark(5, R.string.bookmark_receipt)
    }


    override fun getNavigationGraph(): Int {
        return R.navigation.last_receipt_graph
    }

    private fun goToReceiptFragment() {
        Timber.d("goToReceiptFragment")
        navController.navigate(R.id.action_loaderFragment2_to_receiptViewFragment2)
    }

    private fun goToErrorFragment() {
        Timber.d("goToErrorFragment")
        navController.navigate(R.id.action_loaderFragment2_to_errorFragment2)
    }
}