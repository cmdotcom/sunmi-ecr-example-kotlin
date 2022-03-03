package com.cm.payplaza.ecr_sdk_integration.dialog

import com.cm.payplaza.ecr_sdk_integration.R

class EnableAutoTimezoneDialog(listener: ActionListener): BaseEcrDialog(listener) {
    override fun getDialogString(): Int {
        return R.string.enable_button
    }
}