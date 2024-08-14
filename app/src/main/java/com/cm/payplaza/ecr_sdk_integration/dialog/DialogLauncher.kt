package com.cm.payplaza.ecr_sdk_integration.dialog

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.cm.payplaza.ecr_sdk_integration.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import timber.log.Timber
import java.util.*

class DialogLauncher(
    private val context: Context
) {
    interface ActionListener {
        fun onOkPressed()
        fun onCancelPressed()
        fun onDismiss()
    }

    companion object {
        private var alertDialog: AlertDialog? = null
        private var datePickerDialog: DatePickerDialog? = null
        private var listener: ActionListener? = null
    }

    fun showAlertDialog(
        listener: ActionListener,
        titleStringId: Int? = null,
        hasNegativeButton: Boolean? = false,
        hasPositiveButton: Boolean? = true,
        cancellable: Boolean = true,
        customView: View? = null
    ): AlertDialog {
        dismiss()
        DialogLauncher.listener = listener
        val builder = MaterialAlertDialogBuilder(context)
        if (customView != null) {
            builder.setView(customView)
        } else {
            val titleText = if (titleStringId != null) context.getString(titleStringId) else ""
            builder.setTitle(titleText)
            builder.setCancelable(cancellable)
            if (hasPositiveButton == true) {
                builder.setPositiveButton(R.string.popup_confirm) { _, _ ->
                    listener.onOkPressed()
                    dismiss()
                }
            }
            if (hasNegativeButton == true) {
                builder.setNegativeButton(R.string.close) { _, _ ->
                    listener.onCancelPressed()
                    dismiss()
                }
            }
            builder.setOnCancelListener {
                dismiss()
            }
        }
        val dialog = builder.show().apply {
            hideDialogNavigationBar(this.window)
        }
        alertDialog = dialog
        return dialog
    }

    fun dismiss() {
        Timber.d("dismiss()")
        alertDialog?.dismiss()
        alertDialog = null
        datePickerDialog?.dismiss()
        datePickerDialog = null
        listener?.onDismiss()
        listener = null
    }

    private fun hideDialogNavigationBar(window: Window?) {
        Timber.d("hideAlertDialogNavigationBar()")
        if (window == null) {
            return
        }
        WindowInsetsControllerCompat(
            window,
            window.decorView.findViewById(android.R.id.content)
        ).let { controller ->
            controller.hide(WindowInsetsCompat.Type.navigationBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    fun showDatePickerDialog(
        listener: DatePickerDialog.OnDateSetListener,
        actionListener: ActionListener,
        maxDate: Long? = null,
    ) {
        DialogLauncher.listener = actionListener
        val cal = Calendar.getInstance()
        val dialog = DatePickerDialog(
            context,
            listener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        if (maxDate != null) {
            dialog.datePicker.maxDate = maxDate
        }
        dialog.setOnCancelListener {
            actionListener.onCancelPressed()
        }
        dialog.setOnDismissListener {
            actionListener.onDismiss()
        }
        datePickerDialog = dialog
        dialog.show()
        hideDialogNavigationBar(dialog.window)
    }
}