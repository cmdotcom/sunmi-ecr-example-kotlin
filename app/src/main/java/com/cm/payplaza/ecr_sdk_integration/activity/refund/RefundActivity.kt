package com.cm.payplaza.ecr_sdk_integration.activity.refund

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.activity.base.BaseEcrViewState
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivity
import com.cm.payplaza.ecr_sdk_integration.activity.transactionResult.TransactionResultActivity
import com.cm.payplaza.ecr_sdk_integration.dialog.DialogLauncher
import com.cm.payplaza.ecr_sdk_integration.fragment.amountInsert.AmountInsertFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import com.cm.payplaza.ecr_sdk_integration.fragment.dateInsert.DateInsertState
import com.cm.payplaza.ecr_sdk_integration.fragment.passwordInsert.PasswordInsertFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.stanInsert.StanInsertState
import com.cm.payplaza.ecr_sdk_integration.uicomponents.bottomAppBarComponent.BottomAppBarComponent
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*

class RefundActivity : BaseEcrFragmentActivity<RefundViewModel>() {

    companion object {
        fun start(context: Context) {
            Timber.d("goToRefundActivity")
            val intent = Intent(context, RefundActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

    override val viewModel: RefundViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.progressLoader.visibility = View.GONE
    }

    override fun render(state: BaseEcrViewState) {
        super.render(state)
        when (state) {
            RefundViewState.GoToTransactionActivity -> TransactionResultActivity.start(this)
            RefundViewState.GoToStanInsert -> goToStanInsertFragment()
            RefundViewState.SkipStanInsert -> viewModel.prepareRefund()
        }
    }

    override fun renderFragment(state: BaseEcrFragmentViewState) {
        when (state) {
            is AmountInsertFragmentState.SetupBottomAppBar -> initializeBottomAppBarForAmountInsert(
                state.listener
            )
            is AmountInsertFragmentState.AddDigitToAmountView -> binding.bottomAppView.enableActionButton()
            AmountInsertFragmentState.ClearAmount -> binding.bottomAppView.disableActionButton()
            is AmountInsertFragmentState.NextStep -> {
                viewModel.saveAmount(state.amount)
                viewModel.checkForStanInsert()
            }
            PasswordInsertFragmentState.OkPin -> goToAmountInsertFragment()
            is PasswordInsertFragmentState.SetupBottomAppBar -> setupBottomAppBar(state.listener)
            PasswordInsertFragmentState.ClearPinTokens -> binding.bottomAppView.disableActionButton()
            PasswordInsertFragmentState.ShowWrongPasswordDialog -> showWrongPasswordDialog()
            PasswordInsertFragmentState.EnableContinue -> binding.bottomAppView.enableActionButton()
            PasswordInsertFragmentState.WrongPin -> binding.bottomAppView.disableActionButton()
            is StanInsertState.SaveStand -> saveStand(state.stand)
            is StanInsertState.SetupBottomAppBar -> setupBottomAppBar(state.listener)
            StanInsertState.ClearStan -> binding.bottomAppView.disableActionButton()
            is StanInsertState.StanInserted -> binding.bottomAppView.enableActionButton()
            DateInsertState.ClearDate -> binding.bottomAppView.disableActionButton()
            is DateInsertState.ShowDatePickerDialog -> showDatePickerDialog(state.listener)
            is DateInsertState.UpdateDateView -> dateInserted()
            is DateInsertState.SetupBottomAppBar -> setupBottomAppBar(state.listener)
            is DateInsertState.ConfirmDate -> {
                viewModel.saveDate(state.date)
                viewModel.prepareRefund()
            }
        }
    }

    override fun setUpMenu() {
        super.setUpMenu()
        setMenuStatuses(
            listOf(
                Pair(getString(R.string.refund), false),
                Pair(getString(R.string.cancel_payment), true)
            )
        )
    }

    override fun getNavigationGraph(): Int {
        return R.navigation.refund_graph
    }

    override fun getTransactionTypeStringId(): Int {
        return R.string.bottom_app_bar_refund
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

    private fun showWrongPasswordDialog() {
        Timber.d("showWrongPasswordDialog()")

        val listener = object : DialogLauncher.ActionListener {
            override fun onOkPressed() {
                Timber.d("onOkPressed")
            }

            override fun onCancelPressed() {
                Timber.d("onCancelPressed")
            }

            override fun onDismiss() {
                Timber.d("onDismiss")
                hideNavigationBar()
            }
        }
        DialogLauncher(this).showAlertDialog(listener, R.string.password_error)
    }

    private fun setupBottomAppBar(listener: BottomAppBarComponent.ClickListener) {
        binding.bottomAppView.setButtonsListeners(listener)
        binding.bottomAppView.setActionButtonText(R.string.bottom_button)
        binding.bottomAppView.disableActionButton()
    }

    private fun saveStand(stand: String) {
        Timber.d("saveStand")
        viewModel.saveStan(stand)
        goToDateInsertFragment()
    }

    private fun initializeBottomAppBarForAmountInsert(listener: BottomAppBarComponent.ClickListener) {
        binding.bottomAppView.setActionButtonText(R.string.bottom_app_bar_card_payment_continue)
        binding.bottomAppView.setTransactionTypeText(R.string.bottom_app_bar_refund)
        binding.bottomAppView.setButtonsListeners(listener)
    }

    private fun goToStanInsertFragment() {
        Timber.d("goToStandInsertFragment")
        navController.navigate(R.id.action_amountInsertFragment2_to_stanInsertFragment)
    }

    private fun goToAmountInsertFragment() {
        Timber.d("goToAmountInsertFragment")
        navController.navigate(R.id.action_passwordInsertFragment_to_amountInsertFragment2)
    }

    private fun goToDateInsertFragment() {
        Timber.d("goToDateInsert")
        navController.navigate(R.id.action_stanInsertFragment_to_dateInsertFragment)
    }
}