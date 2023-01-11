package com.cm.payplaza.ecr_sdk_integration.fragment.stanInsert

import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewModel
import com.cm.payplaza.ecr_sdk_integration.uicomponents.bottomAppBarComponent.BottomAppBarComponent

class StanInsertViewModel(
    private val localDataRepository: LocalDataRepository
): BaseEcrFragmentViewModel<StanInsertState>() {
    private var _stanDigits = 0

    override fun init() = updateView(StanInsertState.Init(localDataRepository.getTerminalData()))

    fun clearStan() {
        _stanDigits = 0
        updateView(StanInsertState.ClearStan)
    }

    fun addStanDigit(stanDigit: Int) {
        _stanDigits = (_stanDigits * 10) + stanDigit
        when(_stanDigits.toString().length){
            1 -> updateView(StanInsertState.EnableClean(_stanDigits))
            6 -> updateView(StanInsertState.StanInserted(_stanDigits))
            else -> updateView(StanInsertState.AddStanDigit(_stanDigits))
        }
    }

    fun confirmStan() = updateView(StanInsertState.SaveStand(_stanDigits.toString()))

    fun setupBottomAppBar() {
        val listener = object: BottomAppBarComponent.ClickListener {
            override fun onActionButtonPressed() {
                confirmStan()
            }
            override fun onPrintButtonPressed() {}

        }
        updateView(StanInsertState.SetupBottomAppBar(listener))
    }
}