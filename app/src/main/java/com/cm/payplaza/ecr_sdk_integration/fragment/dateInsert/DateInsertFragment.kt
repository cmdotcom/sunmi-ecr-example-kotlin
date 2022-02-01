package com.cm.payplaza.ecr_sdk_integration.fragment.dateInsert

import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.DatePicker
import com.cm.payplaza.ecr_sdk_integration.databinding.FragmentDateInsertBinding
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragment
import com.cm.payplaza.ecr_sdk_integration.uicomponents.numericKeypadComponent.NumericKeypadComponent
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.*

class DateInsertFragment: BaseEcrFragment<
        DateInsertFramentState,
        DateInsertFragmentViewModel,
        FragmentDateInsertBinding>(),
    DatePickerDialog.OnDateSetListener,
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

    override fun render(state: DateInsertFramentState) {
        when(state){
            is DateInsertFramentState.Init -> initUi(state.merchantData)
            DateInsertFramentState.ClearDate -> clearDate()
            is DateInsertFramentState.UpdateDateView -> updateDateView(state.date)
            else -> { }
        }
    }

    private fun updateDateView(date: Date) {
        Timber.d("updateDateView")
        binding.dateComponentView.setDate(date)
        binding.dateComponentNumericKeypad.enableBackspaceAndConfirm()
    }

    private fun initUi(terminalData: TerminalData?) {
        Timber.d("initUi")
        binding.dateComponentNumericKeypad.setKeypadListener(this)
        binding.dateComponentNumericKeypad.disableNumericKeypad()
        binding.dateComponentNumericKeypad.disableBackspaceAndConfirm()
        binding.dateComponentView.configureForDateInsert()
        binding.dateComponentView.setOnClickListener {
            Timber.d("initUi")
            context?.let {
                val cal = Calendar.getInstance()
                DatePickerDialog(it,
                    this,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH))
                    .show()
            }
        }
        terminalData?.let {
            (terminalData.storeName + "\n" + terminalData.storeAddress + "\n" + terminalData.storeZipCode + " " + terminalData.storeCity).also { binding.dateStoreInfo.text = it }
        }
    }

    private fun clearDate() {
        Timber.d("initUi")
        binding.dateComponentView.clearView()
        binding.dateComponentNumericKeypad.disableNumericKeypad()
        binding.dateComponentNumericKeypad.disableBackspaceAndConfirm()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        Timber.d("onDateSet")
        viewModel.setDate(year, month, dayOfMonth)
    }

    override fun onKeypadDigitPressed(digit: Int) = Timber.d("onKeypadDigitPressed")

    override fun onKeypadBackspacePressed() {
        Timber.d("onKeypadBackspacePressed")
        viewModel.clearDate()
    }

    override fun onKeypadConfirmPressed() {
        Timber.d("onKeypadConfirmPressed")
        viewModel.confirmDate()
    }
}