package com.cm.payplaza.ecr_sdk_integration.fragment.passwordInsert

import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import com.cm.payplaza.ecr_sdk_integration.uicomponents.bottomAppBarComponent.BottomAppBarComponent

sealed class PasswordInsertFragmentState: BaseEcrFragmentViewState() {
    data class Init(val merchantData: TerminalData?): PasswordInsertFragmentState()
    data class SetupBottomAppBar(val listener: BottomAppBarComponent.ClickListener): PasswordInsertFragmentState()
    object ClearPinTokens: PasswordInsertFragmentState()
    object AddPinToken: PasswordInsertFragmentState()
    object OkPin: PasswordInsertFragmentState()
    object WrongPin: PasswordInsertFragmentState()
    object EnableClean: PasswordInsertFragmentState()
    object EnableContinue: PasswordInsertFragmentState()
    object DisableKeyNumbers: PasswordInsertFragmentState()
    object ShowWrongPasswordDialog: PasswordInsertFragmentState()
}