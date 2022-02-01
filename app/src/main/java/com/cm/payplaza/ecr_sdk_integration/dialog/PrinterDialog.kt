package com.cm.payplaza.ecr_sdk_integration.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.cm.payplaza.ecr_sdk_integration.R

class PrinterDialog(listener: ActionListener): BaseEcrDialog(listener) {
    override fun getDialogString(): Int {
        return R.string.tear_off_receipt
    }
}