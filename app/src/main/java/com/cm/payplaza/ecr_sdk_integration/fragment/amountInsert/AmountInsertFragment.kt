package com.cm.payplaza.ecr_sdk_integration.fragment.amountInsert

import android.view.LayoutInflater
import android.view.ViewGroup
import com.cm.payplaza.ecr_sdk_integration.databinding.FragmentAmountInsertBinding
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragment
import com.cm.payplaza.ecr_sdk_integration.uicomponents.numericKeypadComponent.NumericKeypadComponent
import org.koin.android.ext.android.inject
import timber.log.Timber

class AmountInsertFragment: BaseEcrFragment<AmountInsertFragmentState,
        AmountInsertFragmentViewModel,
        FragmentAmountInsertBinding>(),
    NumericKeypadComponent.ClickListener {
    override val viewModel: AmountInsertFragmentViewModel by inject()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAmountInsertBinding {
        return FragmentAmountInsertBinding.inflate(inflater, container, false)
    }

    override fun onResume() {
        super.onResume()
        viewModel.clearAmount()
    }

    override fun render(state: AmountInsertFragmentState) {
        when(state){
            is AmountInsertFragmentState.AddDigitToAmountView -> addDigitToAmountView(state.newInsertedDigits)
            AmountInsertFragmentState.ClearAmount -> clearView()
            is AmountInsertFragmentState.Init -> initializeView(state.data)
            else -> { }
        }
    }

    private fun addDigitToAmountView(newInsertedDigits: Int) {
        Timber.d("addDigitToAmountView")
        binding.componentAmountView.setAmount(newInsertedDigits)
        binding.componentNumericKeypad.enableBackspaceAndConfirm()
        if(newInsertedDigits.toString().length == 7) {
            binding.componentNumericKeypad.disableNumericKeypad()
        } else {
            binding.componentNumericKeypad.enableNumericKeypad()
        }
    }

    private fun initializeView(data: TerminalData?) {
        Timber.d("initializeView")
        binding.componentAmountView.configureForAmountInsert()
        binding.componentNumericKeypad.setKeypadListener(this)
        data?.let {
            (data.storeName + "\n" + data.storeAddress + "\n" + data.storeZipCode + " " + data.storeCity).also { binding.storeInfo.text = it }
        }
    }

    private fun clearView() {
        Timber.d("clearView")
        binding.componentAmountView.setAmount(0)
        binding.componentNumericKeypad.clearKeypad()
    }

    // Numeric component listener
    override fun onKeypadDigitPressed(digit: Int) {
        Timber.d("onKeypadDigitPressed - $digit")
        viewModel.addDigitToAmountView(digit)
    }
    override fun onKeypadBackspacePressed() {
        Timber.d("onKeypadBackspacePressed")
        viewModel.clearAmount()
    }
    override fun onKeypadConfirmPressed() {
        Timber.d("onKeypadConfirmPressed")
        viewModel.doPayment()
    }
}