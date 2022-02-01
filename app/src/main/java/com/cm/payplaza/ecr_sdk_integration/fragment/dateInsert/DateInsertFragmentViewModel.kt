package com.cm.payplaza.ecr_sdk_integration.fragment.dateInsert

import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewModel
import timber.log.Timber
import java.util.*

class DateInsertFragmentViewModel(
        private val localDataRepository: LocalDataRepository
): BaseEcrFragmentViewModel<DateInsertFramentState>() {
    private var _date = Calendar.getInstance()

    override fun init() = updateView(DateInsertFramentState.Init(localDataRepository.getTerminalData()))

    fun clearDate() = updateView(DateInsertFramentState.ClearDate)

    fun confirmDate() = updateView(DateInsertFramentState.ConfirmDate(_date.time))

    fun setDate(year: Int, month: Int, dayOfMonth: Int) {
        _date.set(Calendar.YEAR, year)
        _date.set(Calendar.MONTH, month)
        _date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        Timber.d("setDate - $_date")
        updateView(DateInsertFramentState.UpdateDateView(_date.time))
    }
}