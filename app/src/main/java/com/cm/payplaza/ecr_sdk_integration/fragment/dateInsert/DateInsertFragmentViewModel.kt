package com.cm.payplaza.ecr_sdk_integration.fragment.dateInsert

import android.app.DatePickerDialog
import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewModel
import com.cm.payplaza.ecr_sdk_integration.uicomponents.bottomAppBarComponent.BottomAppBarComponent
import timber.log.Timber
import java.util.*

class DateInsertFragmentViewModel(
        private val localDataRepository: LocalDataRepository
): BaseEcrFragmentViewModel<DateInsertState>() {
    private var _date = Calendar.getInstance()

    override fun init() = updateView(DateInsertState.Init(localDataRepository.getTerminalData()))

    fun clearDate() = updateView(DateInsertState.ClearDate)

    fun confirmDate() = updateView(DateInsertState.ConfirmDate(_date.time))

    private fun setDate(year: Int, month: Int, dayOfMonth: Int) {
        _date.set(Calendar.YEAR, year)
        _date.set(Calendar.MONTH, month)
        _date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        Timber.d("setDate - $_date")
        updateView(DateInsertState.UpdateDateView(_date.time))
    }

    fun setupBottomAppBar() {
        val listener = object: BottomAppBarComponent.ClickListener {
            override fun onActionButtonPressed() {
                confirmDate()
            }

            override fun onPrintButtonPressed() {}
        }
        updateView(DateInsertState.SetupBottomAppBar(listener))
    }

    fun showDatePickerDialog() {
        val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth -> setDate(year, month, dayOfMonth) }
        updateView(DateInsertState.ShowDatePickerDialog(listener))
    }
}