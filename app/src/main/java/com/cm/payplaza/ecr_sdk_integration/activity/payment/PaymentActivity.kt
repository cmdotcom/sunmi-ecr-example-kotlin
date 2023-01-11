package com.cm.payplaza.ecr_sdk_integration.activity.payment

import android.content.Context
import android.content.Intent
import android.view.View
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.activity.base.BaseEcrViewState
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivity
import com.cm.payplaza.ecr_sdk_integration.activity.transactionResult.TransactionResultActivity
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.fragment.amountInsert.AmountInsertFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import com.cm.payplaza.ecr_sdk_integration.fragment.error.ErrorFragmentState
import com.cm.payplaza.ecr_sdk_integration.uicomponents.bottomAppBarComponent.BottomAppBarComponent
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class PaymentActivity : BaseEcrFragmentActivity<PaymentViewModel>() {

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
        when (state) {
            is BaseEcrViewState.RequestInfo -> {
                if (dataLoaded) {
                    setUpVersions(state.terminalData)
                } else {
                    setUpTerminalData(state.terminalData)
                }
            }
            PaymentViewState.GoToTransactionResult -> TransactionResultActivity.start(this)
        }
        super.render(state)
    }

    override fun renderFragment(state: BaseEcrFragmentViewState) {
        when (state) {
            is AmountInsertFragmentState.NextStep -> savePayment(state.amount)
            is AmountInsertFragmentState.SetupBottomAppBar -> initializeActionButtonForAmountInsert(state.listener)
            is AmountInsertFragmentState.AddDigitToAmountView -> binding.bottomAppView.enableActionButton()
            AmountInsertFragmentState.ClearAmount -> binding.bottomAppView.disableActionButton()
            ErrorFragmentState.Dismiss -> supportFragmentManager.popBackStack()
            is ErrorFragmentState.SetUpBottomAppBar -> setUpBottomBarForError(state.listener)
            else -> {Timber.d("$state cached. Do nothing.")}
        }
    }

    override fun setUpMenu() {
        super.setUpMenu()
        setMenuStatuses(
            listOf(
                Pair(getString(R.string.cancel_payment), false),
                Pair(getString(R.string.payment), false)
            )
        )
    }

    override fun initializeView(terminalData: TerminalData?) {
        super.initializeView(terminalData)
        requestInfo()
        initializeTransactionType()
        setDefaultMenuItemSelected()
    }

    private fun setUpBottomBarForError(listener: BottomAppBarComponent.ClickListener) {
        binding.bottomAppView.enableActionButton()
        binding.bottomAppView.setActionButtonText(R.string.bottom_app_bar_card_payment_continue)
        binding.bottomAppView.setButtonsListeners(listener)
    }

    private fun initializeActionButtonForAmountInsert(listener: BottomAppBarComponent.ClickListener) {
        binding.bottomAppView.disableActionButton()
        binding.bottomAppView.setActionButtonText(R.string.bottom_app_bar_card_payment_continue)
        binding.bottomAppView.setButtonsListeners(listener)
    }

    private fun initializeTransactionType() {
        binding.bottomAppView.setTransactionTypeText(R.string.bottom_app_bar_card_payment)
    }

    private fun savePayment(amount: Int) {
        viewModel.savePayment(amount)
    }

    private fun setUpTerminalData(terminalData: TerminalData?) {
        setUpVersions(terminalData)
        goToAmountInsertFragment()
        dataLoaded = true
    }

    override fun getNavigationGraph(): Int {
        return R.navigation.transaction_graph
    }

    override fun restoreActivity() {
        viewModel.error()
        goToErrorFragment()
    }

    private fun goToErrorFragment() {
        navController.popBackStack()
    }

    private fun goToAmountInsertFragment() {
        binding.progressLoader.visibility = View.GONE
        navController.navigate(R.id.action_loaderFragment3_to_amountInsertFragment)
    }
}