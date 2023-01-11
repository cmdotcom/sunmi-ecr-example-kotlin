package com.cm.payplaza.ecr_sdk_integration.activity.lastReceipt

import android.content.Context
import android.content.Intent
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

class LastReceiptActivity : BaseEcrFragmentActivity<LastReceiptViewModel>() {

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
        when (state) {
            LastReceiptState.OnCrash -> goToErrorFragment()
            LastReceiptState.OnError -> goToErrorFragment()
            LastReceiptState.OnResult -> goToReceiptFragment()
        }
    }

    override fun renderFragment(state: BaseEcrFragmentViewState) {
        when (state) {
            ErrorFragmentState.Dismiss -> PaymentActivity.start(this)
            ReceiptState.ControlledTransactionError -> goToErrorFragment()
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
                Pair(getString(R.string.print_last_receipt), false)
            )
        )
    }

    override fun initializeView(terminalData: TerminalData?) {
        super.initializeView(terminalData)
        if (isActivityRestored) {
            restoreActivity()
        } else {
            viewModel.getLastReceipt()
        }
    }

    override fun getNavigationGraph(): Int {
        return R.navigation.last_receipt_graph
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
        binding.bottomAppView.setTransactionTypeText(R.string.bottom_app_bar_last_receipt)
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
        navController.navigate(R.id.action_loaderFragment2_to_receiptViewFragment2)
    }

    private fun goToErrorFragment() {
        binding.progressLoader.visibility = View.GONE
        navController.navigate(R.id.action_loaderFragment2_to_errorFragment2)
    }
}