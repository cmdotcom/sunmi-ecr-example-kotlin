package com.cm.payplaza.ecr_sdk_integration.fragment.error

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.activity.preauth.finish.FinishPreauthActivity
import com.cm.payplaza.ecr_sdk_integration.databinding.FragmentErrorBinding
import com.cm.payplaza.ecr_sdk_integration.dialog.DialogLauncher
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragment
import org.koin.android.ext.android.inject

class ErrorFragment :
    BaseEcrFragment<ErrorFragmentState, ErrorFragmentViewModel, FragmentErrorBinding>() {
    override val viewModel: ErrorFragmentViewModel by inject()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentErrorBinding {
        return FragmentErrorBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity is FinishPreauthActivity) {
            (activity as FinishPreauthActivity).setUpMenu()
            (activity as FinishPreauthActivity).setMenuStatuses(listOf(Pair(getString(R.string.cancel_payment), false)))
        }
    }

    override fun onStart() {
        super.onStart()
        setUpButton()
        viewModel.init()
    }

    override fun render(state: ErrorFragmentState) {
        when (state) {
            is ErrorFragmentState.AutoTimezoneNotEnabled -> enableAutoTimezone(state.messageId)
            is ErrorFragmentState.LowBatteryLevel -> lowBattery(state.messageId)
            is ErrorFragmentState.Error -> showErrorMessage(state.messageId)
            is ErrorFragmentState.BadTimezone -> badTimezone(state.messageId)
            is ErrorFragmentState.TransactionError -> {
                showTransactionErrorMessage(state.message)
            }
            ErrorFragmentState.Dismiss -> { /* Activity takes care */
            }
            is ErrorFragmentState.SetUpBottomAppBar -> { /* Activity takes care */
            }
        }
    }

    private fun showTransactionErrorMessage(message: String) {
        binding.resultLabel.text = message
    }

    private fun showErrorMessage(messageId: Int) {
        binding.resultLabel.text = getString(messageId)
    }

    private fun setUpButton() {
        viewModel.setupBottomAppBar()
    }

    private fun badTimezone(message: Int) {
        showErrorMessage(message)
        activity?.let {
            val listener = object : DialogLauncher.ActionListener {
                override fun onOkPressed() = goToDataAndTimeSettings()
                override fun onCancelPressed() {
                    Toast.makeText(context, R.string.error_bad_timezone_toast, Toast.LENGTH_LONG)
                        .show()
                    viewModel.dismiss()
                }

                override fun onDismiss() {
                    hideNavigationBar()
                }
            }
            DialogLauncher(it).showAlertDialog(listener, R.string.error_bad_timezone_dialog)
        } ?: run { viewModel.dismiss() }
    }

    private fun enableAutoTimezone(message: Int) {
        showErrorMessage(message)
        activity?.let {
            val listener = object : DialogLauncher.ActionListener {
                override fun onOkPressed() = goToDataAndTimeSettings()
                override fun onCancelPressed() {
                    Toast.makeText(
                        context,
                        R.string.error_auto_timezone_not_enabled_button,
                        Toast.LENGTH_LONG
                    ).show()
                    viewModel.dismiss()
                }

                override fun onDismiss() {
                    hideNavigationBar()
                }
            }
            DialogLauncher(it).showAlertDialog(
                listener,
                R.string.error_auto_timezone_not_enabled_button
            )
        } ?: run { viewModel.dismiss() }
    }

    private fun lowBattery(message: Int) {
        showErrorMessage(message)
        Toast.makeText(context, R.string.error_low_battery_connect_device, Toast.LENGTH_LONG).show()
    }

    private fun goToDataAndTimeSettings() {
        val intent = Intent(Settings.ACTION_DATE_SETTINGS)
        startActivity(intent)
    }
}