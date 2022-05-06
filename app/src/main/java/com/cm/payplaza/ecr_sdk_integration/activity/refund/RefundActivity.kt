package com.cm.payplaza.ecr_sdk_integration.activity.refund

import android.content.Context
import android.content.Intent
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.activity.base.BaseEcrViewState
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivity
import com.cm.payplaza.ecr_sdk_integration.activity.payment.PaymentActivity
import com.cm.payplaza.ecr_sdk_integration.activity.transactionResult.TransactionResultActivity
import com.cm.payplaza.ecr_sdk_integration.dialog.BaseEcrDialog
import com.cm.payplaza.ecr_sdk_integration.dialog.CancelDialog
import com.cm.payplaza.ecr_sdk_integration.fragment.amountInsert.AmountInsertFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import com.cm.payplaza.ecr_sdk_integration.fragment.dateInsert.DateInsertFramentState
import com.cm.payplaza.ecr_sdk_integration.fragment.passwordInsert.PasswordInsertFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.stanInsert.StanInsertState
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class RefundActivity: BaseEcrFragmentActivity<RefundViewModel>() {

    companion object {
        fun start(context: Context) {
            Timber.d("goToRefundActivity")
            val intent = Intent(context, RefundActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

    override val viewModel: RefundViewModel by viewModel()

    override fun render(state: BaseEcrViewState) {
        super.render(state)
        when(state) {
            RefundViewState.GoToTransactionActivity -> TransactionResultActivity.start(this)
            RefundViewState.GoToStanInsert -> goToStanInsertFragment()
            RefundViewState.SkipStanInsert -> viewModel.prepareRefund()
        }
    }

    override fun renderFragment(state: BaseEcrFragmentViewState) {
        when(state) {
            is AmountInsertFragmentState.NextStep -> {
                viewModel.saveAmount(state.amount)
                viewModel.checkForStanInsert()
            }
            PasswordInsertFragmentState.OkPin -> goToAmountInsertFragment()
            is StanInsertState.SaveStand -> saveStand(state.stand)
            is DateInsertFramentState.ConfirmDate -> {
                viewModel.saveDate(state.date)
                viewModel.prepareRefund()
            }
        }
    }

    override fun setUpMenu() {
        super.setUpMenu()
        val menu = binding.ecrNavigation.menu
        menu.findItem(R.id.nav_refund).isEnabled = false
        menu.findItem(R.id.nav_cancel_payment).isEnabled = true
    }

    override fun setUpBookmark() {
        binding.ecrBookmarkBar.setSelectedBookmark(1, R.string.refund)
    }

    override fun getNavigationGraph(): Int {
        return R.navigation.refund_graph
    }

    private fun saveStand(stand: String) {
        Timber.d("saveStand")
        viewModel.saveStan(stand)
        goToDateInsertFragment()
    }

    private fun goToStanInsertFragment() {
        Timber.d("goToStandInsertFragment")
        binding.ecrBookmarkBar.setSelectedBookmark(1, R.string.refund)
        navController.navigate(R.id.action_amountInsertFragment2_to_stanInsertFragment)
    }

    private fun goToAmountInsertFragment() {
        binding.ecrBookmarkBar.setSelectedBookmark(1, R.string.refund)
        Timber.d("goToAmountInsertFragment")
        navController.navigate(R.id.action_passwordInsertFragment_to_amountInsertFragment2)
    }

    private fun goToDateInsertFragment() {
        Timber.d("goToDateInsert")
        binding.ecrBookmarkBar.setSelectedBookmark(1, R.string.bookmark_date)
        navController.navigate(R.id.action_stanInsertFragment_to_dateInsertFragment)
    }
}