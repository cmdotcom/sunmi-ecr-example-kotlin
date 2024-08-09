package com.cm.payplaza.ecr_sdk_integration.activity.preauth.finish

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.activity.base.BaseEcrViewState
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivity
import com.cm.payplaza.ecr_sdk_integration.activity.payment.PaymentActivity
import com.cm.payplaza.ecr_sdk_integration.activity.preauth.navigationMenu.MenuItemsDataHolder
import com.cm.payplaza.ecr_sdk_integration.activity.preauth.navigationMenu.PreauthType
import com.cm.payplaza.ecr_sdk_integration.activity.statuses.StatusesActivity
import com.cm.payplaza.ecr_sdk_integration.dialog.DialogLauncher
import com.cm.payplaza.ecr_sdk_integration.fragment.amountInsert.AmountInsertFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import com.cm.payplaza.ecr_sdk_integration.fragment.dateInsert.DateInsertState
import com.cm.payplaza.ecr_sdk_integration.fragment.error.ErrorFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.receiptView.ReceiptState
import com.cm.payplaza.ecr_sdk_integration.fragment.stanInsert.StanInsertState
import com.cm.payplaza.ecr_sdk_integration.uicomponents.bottomAppBarComponent.BottomAppBarComponent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber
import java.util.*

class FinishPreauthActivity : BaseEcrFragmentActivity<FinishPreauthViewModel>(),
    KoinComponent {

    companion object {
        private const val PREAUTH_TYPE_EXTRA = "PREAUTH_TYPE_EXTRA"

        fun start(context: Context, type: PreauthType) {
            Timber.d("goToFinishPreauthActivity")
            val intent = Intent(context, FinishPreauthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra(PREAUTH_TYPE_EXTRA, type)
            context.startActivity(intent)
        }
    }

    private var preAuthType: PreauthType? = null

    override val viewModel: FinishPreauthViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preAuthType = intent.getSerializableExtra(PREAUTH_TYPE_EXTRA) as PreauthType
        viewModel.savePreauthType(preAuthType ?: PreauthType.NONE)

        with(binding.navigationLayout.ecrPreauthExpandibleList) {
            val adapter = MenuItemsDataHolder.getListAdapter(
                context,
                preAuthType ?: PreauthType.NONE
            )
            setOnGroupClickListener { parent, itemView, groupPosition, groupId ->
                menuGroupItemClickListener(parent, itemView, groupPosition, groupId)
            }
            setOnChildClickListener { parent, itemView, groupPosition, childPosition, childId ->
                menuChildItemClickHandler(
                    parent,
                    itemView,
                    groupPosition,
                    childPosition,
                    childId,
                    preAuthType ?: PreauthType.NONE
                )
            }
            setAdapter(adapter)
            visibility = View.VISIBLE
        }
        initializeCloseButton(preAuthType)
        if (preAuthType == PreauthType.CANCEL) {
            skipAmountInsert()
        }
        if (isActivityRestored) {
            StatusesActivity.start(this)
        }
    }

    override fun onStart() {
        super.onStart()
        // Checks if user called for dowload parameters.
        // If that's the case app must restore print button if receipt is being shown.
        restorePrintButton()
    }

    override fun render(state: BaseEcrViewState) {
        super.render(state)
        when (state) {
            FinishPreauthViewState.OnResult -> goToReceipt()
            FinishPreauthViewState.OnCrash -> StatusesActivity.start(this)
            FinishPreauthViewState.OnError -> goToError()
        }
    }

    override fun renderFragment(state: BaseEcrFragmentViewState) {
        when (state) {
            is AmountInsertFragmentState.NextStep -> goToStanInsert(state.amount)
            is AmountInsertFragmentState.SetupBottomAppBar -> initializeBottomAppbar(state.listener)
            is AmountInsertFragmentState.AddDigitToAmountView -> binding.bottomAppView.enableActionButton()
            AmountInsertFragmentState.ClearAmount -> binding.bottomAppView.disableActionButton()
            is StanInsertState.SaveStand -> goToDateInsert(state.stand)
            is StanInsertState.SetupBottomAppBar -> initializeBottomAppbar(state.listener)
            StanInsertState.ClearStan -> binding.bottomAppView.disableActionButton()
            is StanInsertState.StanInserted -> binding.bottomAppView.enableActionButton()
            is DateInsertState.ConfirmDate -> goToLoader(state.date)
            DateInsertState.ClearDate -> binding.bottomAppView.disableActionButton()
            is DateInsertState.ShowDatePickerDialog -> showDatePickerDialog(state.listener)
            is DateInsertState.UpdateDateView -> dateInserted()
            is DateInsertState.SetupBottomAppBar -> initializeBottomAppbar(state.listener)
            ErrorFragmentState.Dismiss -> PaymentActivity.start(this)
            ReceiptState.FinishTransaction -> PaymentActivity.start(this)
            is ReceiptState.SetUpBottomAppBar -> setUpBottomBarForReceipt(state.listener)
            is ReceiptState.Init -> setUpPrinterButton(state.isPrinterAvailable)
            is ErrorFragmentState.SetUpBottomAppBar -> setUpBottomBarForError(state.listener)
            else -> {}
        }
    }

    override fun getNavigationGraph(): Int {
        return R.navigation.finish_preauth_graph
    }

    private fun setUpPrinterButton(printerAvailable: Boolean) {
        binding.bottomAppView.setupPrinterButtonVisibility(printerAvailable)
    }

    private fun setUpBottomBarForError(listener: BottomAppBarComponent.ClickListener) {
        binding.bottomAppView.enableActionButton()
        binding.bottomAppView.setActionButtonText(R.string.bottom_app_bar_card_payment_continue)
        binding.bottomAppView.setButtonsListeners(listener)
    }

    private fun dateInserted() {
        binding.bottomAppView.enableActionButton()
        hideNavigationBar()
    }

    private fun showDatePickerDialog(listener: DatePickerDialog.OnDateSetListener) {
        val actionListener = object : DialogLauncher.ActionListener {
            override fun onOkPressed() {}
            override fun onCancelPressed() {
                hideNavigationBar()
            }

            override fun onDismiss() {
                hideNavigationBar()
            }
        }
        val cal = Calendar.getInstance()
        DialogLauncher(this).showDatePickerDialog(
            listener,
            actionListener,
            cal.timeInMillis
        )
    }

    private fun restorePrintButton() {
        navController.currentDestination?.let {
            // Check if activity is showing receipt fragment
            if (it.id == R.id.receiptViewFragment4) {
                binding.bottomAppView.setPrintButtonText(R.string.print_receipt)
            }
        }
    }

    private fun initializeBottomAppbar(listener: BottomAppBarComponent.ClickListener) {
        binding.bottomAppView.disableActionButton()
        binding.bottomAppView.setActionButtonText(R.string.bottom_app_bar_card_payment_continue)
        binding.bottomAppView.setButtonsListeners(listener)
    }

    private fun initializeCloseButton(preauthType: PreauthType?) {
        val preauthTypeString =
            if (preauthType == PreauthType.CANCEL) R.string.bottom_app_bar_cancel_pre_auth else R.string.bottom_app_bar_confirm_pre_auth
        binding.bottomAppView.setTransactionTypeText(preauthTypeString)
    }

    private fun setUpBottomBarForReceipt(listener: BottomAppBarComponent.ClickListener) {
        binding.bottomAppView.setActionButtonText(R.string.bottom_button)
        binding.bottomAppView.setPrintButtonText(R.string.print_receipt)
        binding.bottomAppView.setButtonsListeners(listener)
        binding.bottomAppView.enableActionButton()
        binding.bottomAppView.setIconsForPrint()
    }

    private fun goToStanInsert(amount: Int) {
        viewModel.saveAmount(amount)
        navController.navigate(R.id.action_amountInsertFragment4_to_stanInsertFragment2)
    }

    private fun goToDateInsert(stan: String) {
        viewModel.saveStan(stan)
        navController.navigate(R.id.action_stanInsertFragment2_to_dateInsertFragment2)
    }

    private fun goToLoader(date: Date) {
        viewModel.saveDate(date)
        binding.progressLoader.visibility = View.VISIBLE
        navController.navigate(R.id.action_dateInsertFragment2_to_loaderFragment5)
        viewModel.doPreauth()
    }

    private fun goToReceipt() {
        binding.progressLoader.visibility = View.GONE
        navController.navigate(R.id.action_loaderFragment5_to_receiptViewFragment4)
    }

    private fun goToError() {
        binding.progressLoader.visibility = View.GONE
        navController.navigate(R.id.action_loaderFragment5_to_errorFragment4)
    }

    private fun skipAmountInsert() {
        navController.navigate(R.id.action_amountInsertFragment4_to_stanInsertFragment2)
    }

    override fun getTransactionTypeStringId(): Int {
        return R.string.bottom_app_bar_confirm_pre_auth
    }
}