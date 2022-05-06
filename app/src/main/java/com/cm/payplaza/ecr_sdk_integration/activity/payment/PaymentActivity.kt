package com.cm.payplaza.ecr_sdk_integration.activity.payment

import android.content.Context
import android.content.Intent
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.activity.base.BaseEcrViewState
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivity
import com.cm.payplaza.ecr_sdk_integration.activity.transactionResult.TransactionResultActivity
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.fragment.amountInsert.AmountInsertFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import com.cm.payplaza.ecr_sdk_integration.fragment.error.ErrorFragmentState
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class PaymentActivity: BaseEcrFragmentActivity<PaymentViewModel>() {

    companion object {
        fun start(context: Context) {
            Timber.d("goToPaymentActivity")
            val intent = Intent(context, PaymentActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

    private var dataLoaded = false
    override val viewModel: PaymentViewModel by viewModel()

    override fun render(state: BaseEcrViewState) {
        when(state) {
            is BaseEcrViewState.RequestInfo -> {
                if(dataLoaded) { setUpVersions(state.terminalData) }
                else { setUpTerminalData(state.terminalData) }
            }
            PaymentViewState.GoToTransactionResult -> TransactionResultActivity.start(this)
        }
        super.render(state)
    }

    override fun renderFragment(state: BaseEcrFragmentViewState) {
        when(state) {
            is AmountInsertFragmentState.NextStep -> savePayment(state.amount)
            ErrorFragmentState.Dismiss -> supportFragmentManager.popBackStack()
        }
    }

    override fun setUpMenu() {
        super.setUpMenu()
        val menu = binding.ecrNavigation.menu
        menu.findItem(R.id.nav_cancel_payment).isEnabled = false
        menu.findItem(R.id.nav_new_payment).isEnabled = false
    }

    override fun initializeView(terminalData: TerminalData?) {
        super.initializeView(terminalData)
        requestInfo()
    }

    private fun savePayment(amount: Int) {
        viewModel.savePayment(amount)
    }

    private fun setUpTerminalData(terminalData: TerminalData?) {
        setUpVersions(terminalData)
        goToAmountInsertFragment()
        dataLoaded = true
    }

    override fun setUpBookmark() {
        Timber.d("setUpBookmark")
        binding.ecrBookmarkBar.setSelectedBookmark(1, R.string.payment)
    }

    override fun getNavigationGraph(): Int {
        return R.navigation.transaction_graph
    }

    private fun goToAmountInsertFragment() {
        Timber.d("goToAmountInsertFragment")
        navController.navigate(R.id.action_loaderFragment3_to_amountInsertFragment)
    }
}