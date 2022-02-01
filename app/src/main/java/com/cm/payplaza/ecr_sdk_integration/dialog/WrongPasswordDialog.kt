package com.cm.payplaza.ecr_sdk_integration.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.cm.payplaza.ecr_sdk_integration.R

class WrongPasswordDialog: DialogFragment() {
    override fun onCreateDialog( savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.password_error)
                .setPositiveButton(R.string.ok
                ) { _, _ -> dismiss() }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}