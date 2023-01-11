package com.cm.payplaza.ecr_sdk_integration.fragment.stanInsert

import android.view.LayoutInflater
import android.view.ViewGroup
import com.cm.payplaza.ecr_sdk_integration.databinding.FragmentStanInsertBinding
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragment
import com.cm.payplaza.ecr_sdk_integration.uicomponents.numericKeypadComponent.NumericKeypadComponent
import org.koin.android.ext.android.inject
import timber.log.Timber

class StanInsertFragment: BaseEcrFragment<
        StanInsertState,
        StanInsertViewModel,
        FragmentStanInsertBinding>(),
    NumericKeypadComponent.ClickListener {
    override val viewModel: StanInsertViewModel by inject()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStanInsertBinding {
        return FragmentStanInsertBinding.inflate(inflater, container, false)
    }

    override fun onResume() {
        super.onResume()
        viewModel.clearStan()
    }

    override fun render(state: StanInsertState) {
        when(state) {
            is StanInsertState.Init -> initializeView()
            StanInsertState.ClearStan -> clearStan()
            is StanInsertState.EnableClean -> enableClean(state.stanDigits)
            is StanInsertState.StanInserted -> stanInserted(state.stanDigits)
            is StanInsertState.AddStanDigit -> binding.stanComponentView.addStanDigit(state.stanDigits)
            else -> { }
        }
    }

    private fun stanInserted(stanDigits: Int) {
        Timber.d("stanInserted - $stanDigits")
        binding.stanComponentNumericKeypad.disableNumericKeypad()
        binding.stanComponentView.addStanDigit(stanDigits)
        binding.stanComponentView.enableAmountViewShape()
    }

    private fun enableClean(stanDigits: Int) {
        Timber.d("enableClean - $stanDigits")
        binding.stanComponentNumericKeypad.enableBackspace()
        binding.stanComponentView.addStanDigit(stanDigits)
    }

    private fun clearStan() {
        Timber.d("clearStan")
        binding.stanComponentNumericKeypad.clearKeypad()
        binding.stanComponentView.clearView()
        binding.stanComponentView.disableAmountViewShape()
    }

    private fun initializeView() {
        Timber.d("initializeView")
        binding.stanComponentView.configureForStanInsert()
        binding.stanComponentNumericKeypad.setKeypadListener(this)
        viewModel.setupBottomAppBar()
    }

    override fun onKeypadDigitPressed(digit: Int) {
        Timber.d("onKeypadDigitPressed")
        viewModel.addStanDigit(digit)
    }

    override fun onKeypadBackspacePressed() {
        Timber.d("onKeypadBackspacePressed")
        viewModel.clearStan()
    }
}