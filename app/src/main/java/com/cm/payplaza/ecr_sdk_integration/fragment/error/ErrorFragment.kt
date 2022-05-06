package com.cm.payplaza.ecr_sdk_integration.fragment.error

import android.content.Intent
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.databinding.FragmentErrorBinding
import com.cm.payplaza.ecr_sdk_integration.dialog.BaseEcrDialog
import com.cm.payplaza.ecr_sdk_integration.dialog.EnableAutoTimezoneDialog
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragment
import org.koin.android.ext.android.inject
import timber.log.Timber

class ErrorFragment: BaseEcrFragment<ErrorFragmentState, ErrorFragmentViewModel, FragmentErrorBinding>() {
    override val viewModel: ErrorFragmentViewModel by inject()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentErrorBinding {
        return FragmentErrorBinding.inflate(inflater, container, false)
    }

    override fun onStart() {
        super.onStart()
        setUpButton()
    }

    override fun render(state: ErrorFragmentState) {
        when(state) {
            ErrorFragmentState.AutoTimezoneNotEnabled -> askForEnableAutoTimezone()
            ErrorFragmentState.LowBatteryLevel -> setUpLowBatteryError()
            else -> { setUpUnkownError() }
        }
    }

    private fun setUpButton() {
        Timber.d("setUpButton")
        binding.errorContinueButton.setOnClickListener {
            viewModel.dismiss()
        }
    }

    private fun setUpUnkownError() {
        binding.errorHeader.text = getString(R.string.error_occurred)
    }

    private fun askForEnableAutoTimezone() {
        binding.errorHeader.text = getString(R.string.error_auto_timezone_not_enabled)
        activity?.let {
            val listener = object : BaseEcrDialog.ActionListener {
                override fun onOkPressed() = goToDataAndTimeSettings()
                override fun onCancelPressed() {
                    Toast.makeText(context, R.string.enable_button, Toast.LENGTH_LONG).show()
                    viewModel.dismiss()
                }
                override fun onDismiss() {}
            }
            EnableAutoTimezoneDialog(listener)
                .show(it.supportFragmentManager,"")
        } ?: run { viewModel.dismiss() }
    }

    private fun goToDataAndTimeSettings() {
        val intent = Intent(Settings.ACTION_DATE_SETTINGS)
        startActivity(intent)
    }

    private fun setUpLowBatteryError() {
        binding.errorHeader.text = getString(R.string.error_low_battery)
        Toast.makeText(context, R.string.error_low_battery_connect_device, Toast.LENGTH_LONG).show()
    }
}