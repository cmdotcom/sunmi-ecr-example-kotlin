package com.cm.payplaza.ecr_sdk_integration.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.cm.payplaza.ecr_sdk_integration.R

abstract class BaseEcrDialog (private val listener: ActionListener): DialogFragment() {
    interface ActionListener {
        fun onOkPressed()
        fun onCancelPressed()
    }
    override fun onCreateDialog( savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(getDialogString())
            builder.setPositiveButton(R.string.ok) { _, _ ->
                listener.onOkPressed()
                dismiss()
            }
            builder.setNegativeButton(R.string.cancel_button) { _, _ ->
                listener.onCancelPressed()
                dismiss()
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    abstract fun getDialogString(): Int
}