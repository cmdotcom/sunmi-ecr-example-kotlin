package com.cm.payplaza.ecr_sdk_integration.activity.transactionResult

import android.content.Context
import android.content.Intent
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.activity.base.BaseEcrViewState
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivity
import com.cm.payplaza.ecr_sdk_integration.activity.payment.PaymentActivity
import com.cm.payplaza.ecr_sdk_integration.activity.statuses.StatusesActivity
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import com.cm.payplaza.ecr_sdk_integration.fragment.error.ErrorFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.receiptView.ReceiptViewFragmentState
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class TransactionResultActivity: BaseEcrFragmentActivity<TransactionResultViewModel>() {

    companion object {
        fun start(context: Context) {
            Timber.d("goToTotals")
            val intent = Intent(context, TransactionResultActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

    override val viewModel: TransactionResultViewModel by viewModel()

    override fun render(state: BaseEcrViewState) {
        super.render(state)
        when(state) {
            TransactionResultState.GoToReceiptFragment -> goToReceiptFragment()
            TransactionResultState.OnResult -> goToReceiptFragment()
            TransactionResultState.OnFinishTransaction -> PaymentActivity.start(this)
            TransactionResultState.OnCrash -> goToErrorFragment()
            TransactionResultState.OnError -> goToErrorFragment()
            TransactionResultState.GoToStatuses -> StatusesActivity.start(this)
        }
    }

    override fun renderFragment(state: BaseEcrFragmentViewState) {
        when(state) {
            ErrorFragmentState.Dismiss -> PaymentActivity.start(this)
            ReceiptViewFragmentState.ControlledTransactionError -> goToErrorFragment()
            ReceiptViewFragmentState.FinishTransaction -> PaymentActivity.start(this)
        }
    }

    override fun initializeView(terminalData: TerminalData?) {
        super.initializeView(terminalData)
        viewModel.doTransaction()
    }

    override fun setUpBookmark() {
        Timber.d("setUpBookmark")
        binding.ecrBookmarkBar.setSelectedBookmark(5, R.string.settings_receipts)
    }

    override fun getNavigationGraph(): Int {
        return R.navigation.nav_graph
    }

    private fun goToReceiptFragment() {
        Timber.d("goToReceiptFragment")
        setUpBookmark()
        navController.navigate(R.id.action_loaderFragment_to_receiptViewFragment)
    }

    private fun goToErrorFragment() {
        Timber.d("goToErrorFragment")
        setUpBookmark()
        navController.navigate(R.id.action_loaderFragment_to_errorFragment)
    }
}