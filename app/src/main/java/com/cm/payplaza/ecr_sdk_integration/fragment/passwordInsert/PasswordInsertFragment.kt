package com.cm.payplaza.ecr_sdk_integration.fragment.passwordInsert

import android.view.LayoutInflater
import android.view.ViewGroup
import com.cm.payplaza.ecr_sdk_integration.databinding.FragmentPasswordInsertBinding
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragment
import com.cm.payplaza.ecr_sdk_integration.uicomponents.numericKeypadComponent.NumericKeypadComponent
import org.koin.android.ext.android.inject
import timber.log.Timber

class PasswordInsertFragment: BaseEcrFragment<
        PasswordInsertFragmentState,
        PasswordInsertFragmentViewModel,
        FragmentPasswordInsertBinding>(),
    NumericKeypadComponent.ClickListener {
    override val viewModel: PasswordInsertFragmentViewModel by inject()

    override fun onResume() {
        super.onResume()
        viewModel.clearPinTokens()
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPasswordInsertBinding {
        return FragmentPasswordInsertBinding.inflate(inflater, container, false)
    }

    override fun render(state: PasswordInsertFragmentState) {
        when(state) {
            is PasswordInsertFragmentState.Init -> initializeView()
            PasswordInsertFragmentState.AddPinToken -> binding.componentPinView.addPasswordToken()
            PasswordInsertFragmentState.ClearPinTokens -> clearComponents()
            PasswordInsertFragmentState.DisableKeyNumbers -> disableKeyNumbers()
            PasswordInsertFragmentState.EnableClean -> enableClean()
            PasswordInsertFragmentState.EnableContinue -> enableContinue()
            PasswordInsertFragmentState.WrongPin -> {
                clearComponents()
                showWrongPinSnackbar()
            }
            else -> { }
        }
    }

    private fun enableContinue() {
        Timber.d("enableContinue")
        binding.componentPinView.addPasswordToken()
    }

    private fun enableClean() {
        Timber.d("enableClean")
        binding.componentNumericKeypad.enableBackspace()
        binding.componentPinView.addPasswordToken()
        binding.componentPinView.enableAmountViewShape()
    }

    private fun disableKeyNumbers() {
        Timber.d("disableKeyNumbers")
        binding.componentNumericKeypad.disableNumericKeypad()
        binding.componentPinView.addPasswordToken()
    }

    private fun initializeView() {
        Timber.d("initializeView")
        binding.componentPinView.configureForPasswordInsert()
        binding.componentNumericKeypad.setKeypadListener(this)
        viewModel.setupBottomAppBar()
    }

    private fun clearComponents() {
        Timber.d("clearComponents")
        binding.componentPinView.clearView()
        binding.componentNumericKeypad.clearKeypad()
        binding.componentPinView.disableAmountViewShape()
    }

    private fun showWrongPinSnackbar() {
        Timber.d("showWrongPinSnackbar")
        viewModel.showWrongPasswordDialog()
    }

    override fun onKeypadDigitPressed(digit: Int) {
        Timber.d("onKeypadDigitPressed")
        viewModel.addPinToken(digit)
    }

    override fun onKeypadBackspacePressed() {
        Timber.d("onKeypadBackspacePressed")
        viewModel.clearPinTokens()
    }
}