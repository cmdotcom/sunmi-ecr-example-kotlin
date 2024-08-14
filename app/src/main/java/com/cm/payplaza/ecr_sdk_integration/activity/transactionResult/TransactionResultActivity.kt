package com.cm.payplaza.ecr_sdk_integration.activity.transactionResult

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.cm.androidposintegration.enums.TransactionType
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.activity.base.BaseEcrViewState
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivity
import com.cm.payplaza.ecr_sdk_integration.activity.payment.PaymentActivity
import com.cm.payplaza.ecr_sdk_integration.activity.statuses.StatusesActivity
import com.cm.payplaza.ecr_sdk_integration.dialog.DialogLauncher
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import com.cm.payplaza.ecr_sdk_integration.fragment.error.ErrorFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.receiptView.ReceiptState
import com.cm.payplaza.ecr_sdk_integration.uicomponents.bottomAppBarComponent.BottomAppBarComponent
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

class TransactionResultActivity : BaseEcrFragmentActivity<TransactionResultViewModel>() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, TransactionResultActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

    override val viewModel: TransactionResultViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!isActivityRestored) {
            viewModel.checkPrinterStatus()
        } else {
            StatusesActivity.start(this)
        }
    }

    override fun onStart() {
        super.onStart()
        if(downloadingParameters) {
            viewModel.restoreBottomAppbar()
        }
    }

    override fun render(state: BaseEcrViewState) {
        super.render(state)
        when (state) {
            TransactionResultState.GoToReceiptFragment -> goToReceiptFragment()
            TransactionResultState.OnResult -> goToReceiptFragment()
            TransactionResultState.OnFinishTransaction -> PaymentActivity.start(this)
            TransactionResultState.OnCrash -> goToErrorFragment()
            TransactionResultState.OnError -> goToErrorFragment()
            TransactionResultState.GoToStatuses -> StatusesActivity.start(this)
            is TransactionResultState.SetTransactionType -> setTransactionTypeInBottomAppBar(state.transactionType)
            TransactionResultState.ShowWarningPopupPrinterOutOfPaper -> showWarningPopupPrinterOutOfPaper()
            is TransactionResultState.RestoreBottomAppBar -> setPrintButtonVisivility(state.isPrinterOutOfPaper)
        }
    }

    override fun renderFragment(state: BaseEcrFragmentViewState) {
        when (state) {
            ErrorFragmentState.Dismiss -> PaymentActivity.start(this)
            ReceiptState.ControlledTransactionError -> goToErrorFragment()
            ReceiptState.FinishTransaction -> PaymentActivity.start(this)
            is ReceiptState.SetUpBottomAppBar -> setUpBottomBarForReceipt(state.listener, state.isPrinterAvailable)
            is ReceiptState.Init -> {
                setUpPrinterButton(state.isPrinterAvailable)
                showTransactionAmount(state.transactionAmount, state.currency)
            }
            is ReceiptState.PrinterOutOfPaper -> showPrinterOutOfPaperPopup(state.listener)
            is ErrorFragmentState.SetUpBottomAppBar -> setUpBottomBarForError(state.listener)
        }
    }

    private fun showPrinterOutOfPaperPopup(listener: DialogLauncher.ActionListener) {
        val dialogLauncher = DialogLauncher(this)
        dialogLauncher.showAlertDialog(
            listener,
            R.string.printer_out_of_paper_popup,
            hasNegativeButton = true,
            hasPositiveButton = true,
            cancellable = false,
            customView = null
        )
    }

    private fun showWarningPopupPrinterOutOfPaper() {
        viewModel.setTransactionType()
        binding.bottomAppView.hideActionButton()
        val listener = object: DialogLauncher.ActionListener {
            override fun onOkPressed() {
                viewModel.doTransaction()
            }
            override fun onCancelPressed() {}
            override fun onDismiss() { hideNavigationBar() }
        }
        val dialogLauncher = DialogLauncher(this)
        dialogLauncher.showAlertDialog(
            listener,
            R.string.printer_out_of_paper_popup,
            hasNegativeButton = false,
            hasPositiveButton = true,
            cancellable = false,
            customView = null
        )
    }

    override fun setUpMenu() {
        super.setUpMenu()
        setMenuStatuses(
            listOf(
                Pair(getString(R.string.cancel_payment), false),
            )
        )
    }

    override fun getNavigationGraph(): Int {
        return R.navigation.nav_graph
    }

    private fun setUpPrinterButton(printerAvailable: Boolean) {
        binding.bottomAppView.setupPrinterButtonVisibility(printerAvailable)
    }

    private fun showTransactionAmount(amount: BigDecimal, currency: Currency) {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
        numberFormat.currency = currency

        binding.amountToolbar.amountAppbarLayout.visibility = View.VISIBLE
        binding.progressIndicatorDivider.visibility = View.GONE
        binding.amountToolbar.labelTransactionAmount.text = getString(
            R.string.amount_appbar_title,
            numberFormat.format(amount)
        )
    }

    private fun setTransactionTypeInBottomAppBar(transactionType: TransactionType) {
        val transactioTypeText = getTransactionTypeString(transactionType)
        binding.bottomAppView.setTransactionTypeText(transactioTypeText)
    }

    private fun getTransactionTypeString(transactionType: TransactionType): Int {
        return when (transactionType) {
            TransactionType.PURCHASE -> R.string.bottom_app_bar_card_payment
            TransactionType.REFUND -> R.string.bottom_app_bar_refund
            TransactionType.PRE_AUTH -> R.string.bottom_app_bar_start_pre_auth
        }
    }

    private fun setUpBottomBarForError(listener: BottomAppBarComponent.ClickListener) {
        binding.bottomAppView.enableActionButton()
        binding.bottomAppView.setActionButtonText(R.string.bottom_app_bar_card_payment_continue)
        binding.bottomAppView.clearPrintButtonText()
        binding.bottomAppView.setButtonsListeners(listener)
        viewModel.setTransactionType()
    }

    private fun setUpBottomBarForReceipt(
        listener: BottomAppBarComponent.ClickListener,
        isPrinterAvailable: Boolean
    ) {
        binding.bottomAppView.setButtonsListeners(listener)
        binding.bottomAppView.enableActionButton()
        binding.bottomAppView.setActionButtonText(R.string.bottom_button)
        if(isPrinterAvailable) {
            binding.bottomAppView.setPrintButtonText(R.string.print_receipt)
            binding.bottomAppView.setIconsForPrint()
        } else {
            binding.bottomAppView.hidePrintButton()
        }
    }

    private fun setPrintButtonVisivility(isPrinterOutOfPaper: Boolean) {
        if(isPrinterOutOfPaper) {
            binding.bottomAppView.hidePrintButton()
            binding.bottomAppView.hideTransactionTypeText()
        } else {
            binding.bottomAppView.setPrintButtonText(R.string.print_receipt)
            binding.bottomAppView.setIconsForPrint()
        }
    }

    private fun goToReceiptFragment() {
        binding.progressLoader.visibility = View.GONE
        navController.navigate(R.id.action_loaderFragment_to_receiptViewFragment)
    }

    private fun goToErrorFragment() {
        binding.progressLoader.visibility = View.GONE
        navController.navigate(R.id.action_loaderFragment_to_errorFragment)
    }

    override fun getTransactionTypeStringId(): Int {
        return R.string.bottom_app_bar_card_payment
    }
}