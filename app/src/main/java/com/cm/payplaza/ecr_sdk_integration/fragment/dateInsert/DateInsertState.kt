package com.cm.payplaza.ecr_sdk_integration.fragment.dateInsert

import android.app.DatePickerDialog
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import com.cm.payplaza.ecr_sdk_integration.uicomponents.bottomAppBarComponent.BottomAppBarComponent
import java.util.*

sealed class DateInsertState: BaseEcrFragmentViewState() {
    data class Init(val merchantData: TerminalData?): DateInsertState()
    object ClearDate: DateInsertState()
    data class UpdateDateView(val date: Date): DateInsertState()
    data class ConfirmDate(val date: Date): DateInsertState()
    data class SetupBottomAppBar(val listener: BottomAppBarComponent.ClickListener): DateInsertState()
    data class ShowDatePickerDialog(val listener: DatePickerDialog.OnDateSetListener): DateInsertState()
}