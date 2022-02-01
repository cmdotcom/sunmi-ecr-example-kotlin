package com.cm.payplaza.ecr_sdk_integration.dialog

import com.cm.payplaza.ecr_sdk_integration.R

class RequestInfoFailedDialog (listener: ActionListener): BaseEcrDialog(listener) {
    override fun getDialogString(): Int {
        return R.string.request_info_dialog
    }
}