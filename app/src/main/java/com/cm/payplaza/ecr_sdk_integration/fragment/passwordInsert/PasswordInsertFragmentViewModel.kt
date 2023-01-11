package com.cm.payplaza.ecr_sdk_integration.fragment.passwordInsert

import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewModel
import com.cm.payplaza.ecr_sdk_integration.uicomponents.bottomAppBarComponent.BottomAppBarComponent
import com.cm.payplaza.ecr_sdk_integration.utils.Configuration
import timber.log.Timber

class PasswordInsertFragmentViewModel(
    private val localDataRepository: LocalDataRepository
): BaseEcrFragmentViewModel<PasswordInsertFragmentState>() {
    private var _password: Int = 0
    override fun init() = updateView(PasswordInsertFragmentState.Init(localDataRepository.getTerminalData()))

    fun addPinToken(digit: Int) {
        this._password = (_password * 10) + digit
        when (_password.toString().length) {
            1 -> updateView(PasswordInsertFragmentState.EnableClean)
            3 -> updateView(PasswordInsertFragmentState.EnableContinue)
            8 -> updateView(PasswordInsertFragmentState.DisableKeyNumbers)
            else -> updateView(PasswordInsertFragmentState.AddPinToken)
        }
        Timber.d("addPinToken - $_password")
    }

    fun clearPinTokens() {
        _password = 0
        updateView(PasswordInsertFragmentState.ClearPinTokens)
    }

    fun showWrongPasswordDialog() {
        updateView(PasswordInsertFragmentState.ShowWrongPasswordDialog)
    }

    private fun checkPin() {
        if(Configuration.REFUND_PIN == _password.toString()) {
            updateView(PasswordInsertFragmentState.OkPin)
        } else {
            _password = 0
            updateView(PasswordInsertFragmentState.WrongPin)
        }
    }

    fun setupBottomAppBar() {
        val listener = object: BottomAppBarComponent.ClickListener {
            override fun onActionButtonPressed() {
                Timber.d("onKeypadConfirmPressed")
                checkPin()
            }
            override fun onPrintButtonPressed() {}

        }
        updateView(PasswordInsertFragmentState.SetupBottomAppBar(listener))
    }
}