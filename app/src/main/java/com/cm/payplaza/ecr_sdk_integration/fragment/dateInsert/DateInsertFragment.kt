package com.cm.payplaza.ecr_sdk_integration.fragment.dateInsert

import android.view.LayoutInflater
import android.view.ViewGroup
import com.cm.payplaza.ecr_sdk_integration.databinding.FragmentDateInsertBinding
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragment
import com.cm.payplaza.ecr_sdk_integration.uicomponents.numericKeypadComponent.NumericKeypadComponent
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.*

class DateInsertFragment: BaseEcrFragment<
        DateInsertState,
        DateInsertFragmentViewModel,
        FragmentDateInsertBinding>(),
    NumericKeypadComponent.ClickListener {
    override val viewModel: DateInsertFragmentViewModel by inject()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDateInsertBinding {
        return FragmentDateInsertBinding.inflate(inflater, container, false)
    }

    override fun onResume() {
        super.onResume()
        viewModel.clearDate()
    }

    override fun render(state: DateInsertState) {
        when(state){
            is DateInsertState.Init -> initUi()
            DateInsertState.ClearDate -> clearDate()
            is DateInsertState.UpdateDateView -> updateDateView(state.date)
            else -> { }
        }
    }

    private fun updateDateView(date: Date) {
        Timber.d("updateDateView")
        binding.dateComponentView.setDate(date)
        binding.dateComponentNumericKeypad.enableBackspaceAndConfirm()
        binding.dateComponentView.enableAmountViewShape()
    }

    private fun initUi() {
        Timber.d("initUi")
        binding.dateComponentNumericKeypad.setKeypadListener(this)
        binding.dateComponentNumericKeypad.disableNumericKeypad()
        binding.dateComponentNumericKeypad.disableBackspaceAndConfirm()
        binding.dateComponentView.configureForDateInsert()
        binding.dateComponentView.setOnClickListener { viewModel.showDatePickerDialog() }
        binding.dateComponentView.disableAmountViewShape()
        viewModel.setupBottomAppBar()
    }

    private fun clearDate() {
        Timber.d("initUi")
        binding.dateComponentView.clearView()
        binding.dateComponentNumericKeypad.disableNumericKeypad()
        binding.dateComponentNumericKeypad.disableBackspaceAndConfirm()
        binding.dateComponentView.disableAmountViewShape()
    }

    override fun onKeypadDigitPressed(digit: Int) = Timber.d("onKeypadDigitPressed")

    override fun onKeypadBackspacePressed() {
        Timber.d("onKeypadBackspacePressed")
        viewModel.clearDate()
    }
}