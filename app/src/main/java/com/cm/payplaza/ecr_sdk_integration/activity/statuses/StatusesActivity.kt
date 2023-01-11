package com.cm.payplaza.ecr_sdk_integration.activity.statuses

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.view.GravityCompat
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.activity.base.BaseEcrViewState
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivity
import com.cm.payplaza.ecr_sdk_integration.activity.lastReceipt.LastReceiptActivity
import com.cm.payplaza.ecr_sdk_integration.activity.payment.PaymentActivity
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import com.cm.payplaza.ecr_sdk_integration.fragment.error.ErrorFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.receiptView.ReceiptState
import com.cm.payplaza.ecr_sdk_integration.fragment.statuses.StatusesFragmentState
import com.cm.payplaza.ecr_sdk_integration.uicomponents.bottomAppBarComponent.BottomAppBarComponent
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class StatusesActivity : BaseEcrFragmentActivity<StatusesViewModel>() {

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
        when (state) {
            StatusesState.OnResult -> goToStatusesFragment()
            StatusesState.OnCrash -> goToErrorFragment()
            StatusesState.OnError -> goToErrorFragment()
        }
    }

    override fun renderFragment(state: BaseEcrFragmentViewState) {
        when (state) {
            StatusesFragmentState.NoDataAvailable -> goToErrorFromStatuses()
            StatusesFragmentState.GoToReceiptView -> goToReceiptView()
            StatusesFragmentState.GoToLastReceipt -> LastReceiptActivity.start(this)
            StatusesFragmentState.GoToPayment -> PaymentActivity.start(this)
            is StatusesFragmentState.SetupBottomAppBar -> setupBottomAppBar(state.listener)
            ErrorFragmentState.Dismiss -> PaymentActivity.start(this)
            ReceiptState.FinishTransaction -> goToStatusesFromReceipt()
            ReceiptState.ControlledTransactionError -> goToStatusesFromReceipt()
            is ErrorFragmentState.SetUpBottomAppBar -> setUpBottomBarForError(state.listener)
            else -> { }
        }
    }

    private fun setupBottomAppBar(listener: BottomAppBarComponent.ClickListener) {
        binding.bottomAppView.setActionButtonText(R.string.bottom_button)
        binding.bottomAppView.enableActionButton()
        binding.bottomAppView.setButtonsListeners(listener)
        binding.bottomAppView.hidePrintButton()
        binding.bottomAppView.hideTransactionTypeText()
    }

    private fun setUpBottomBarForError(listener: BottomAppBarComponent.ClickListener) {
        binding.bottomAppView.enableActionButton()
        binding.bottomAppView.setActionButtonText(R.string.bottom_app_bar_card_payment_continue)
        binding.bottomAppView.setTransactionTypeText(R.string.bottom_app_bar_statuses)
        binding.bottomAppView.setButtonsListeners(listener)
    }

    override fun setUpMenu() {
        super.setUpMenu()
        binding.navigationLayout.navView.setNavigationItemSelectedListener {
            binding.drawer.closeDrawer(GravityCompat.START)
            false
        }
        setMenuStatuses(
            listOf(
                Pair(getString(R.string.cancel_payment), false),
                Pair(getString(R.string.print_last_receipt), false),
                Pair(getString(R.string.day_totals), false),
                Pair(getString(R.string.payment), false),
                Pair(getString(R.string.refund), false)
            )
        )
    }

    override fun initializeView(terminalData: TerminalData?) {
        super.initializeView(terminalData)
        if (isActivityRestored) {
            restoreActivity()
        } else {
            viewModel.getStatuses()
        }
    }

    override fun getNavigationGraph(): Int {
        return R.navigation.statuses_graph
    }

    override fun restoreActivity() {
        viewModel.error()
        goToErrorFromStatuses()
    }

    private fun goToErrorFragment() {
        binding.progressLoader.visibility = View.GONE
        navController.navigate(R.id.action_loaderFragment4_to_errorFragment3)
    }

    private fun goToErrorFromStatuses() {
        navController.navigate(R.id.action_statusesFragment_to_errorFragment3)
    }

    private fun goToStatusesFragment() {
        binding.progressLoader.visibility = View.GONE
        navController.navigate(R.id.action_loaderFragment4_to_statusesFragment)
    }

    private fun goToReceiptView() {
        val listener = object: BottomAppBarComponent.ClickListener {
            override fun onActionButtonPressed() {
                goToStatusesFromReceipt()
            }

            override fun onPrintButtonPressed() {}

        }
        setupBottomAppBar(listener)
        Timber.d("goToReceiptView")
        navController.navigate(R.id.action_statusesFragment_to_receiptViewFragment3)
    }

    private fun goToStatusesFromReceipt() {
        navController.navigate(R.id.action_receiptViewFragment3_to_statusesFragment4)
    }
}