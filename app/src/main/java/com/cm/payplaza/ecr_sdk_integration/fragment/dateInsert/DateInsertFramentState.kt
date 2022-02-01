package com.cm.payplaza.ecr_sdk_integration.fragment.dateInsert

import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import java.util.*

sealed class DateInsertFramentState: BaseEcrFragmentViewState() {
    data class Init(val merchantData: TerminalData?): DateInsertFramentState()
    object ClearDate: DateInsertFramentState()
    data class UpdateDateView(val date: Date): DateInsertFramentState()
    data class ConfirmDate(val date: Date): DateInsertFramentState()
}