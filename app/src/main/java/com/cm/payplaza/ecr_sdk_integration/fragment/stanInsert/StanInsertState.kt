package com.cm.payplaza.ecr_sdk_integration.fragment.stanInsert

import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState

sealed class StanInsertState: BaseEcrFragmentViewState() {
    data class Init(val merchantData: TerminalData?): StanInsertState()
    object ClearStan: StanInsertState()
    data class EnableClean(val stanDigits: Int): StanInsertState()
    data class StanInserted(val stanDigits: Int): StanInsertState()
    data class AddStanDigit(val stanDigits: Int): StanInsertState()
    data class SaveStand(val stand: String): StanInsertState()
}