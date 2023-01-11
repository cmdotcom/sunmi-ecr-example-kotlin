package com.cm.payplaza.ecr_sdk_integration.uicomponents.bottomAppBarComponent

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.LinearLayoutCompat
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.databinding.ComponentBottomAppBarBinding
import org.koin.core.component.KoinComponent

class BottomAppBarComponent : LinearLayoutCompat, KoinComponent {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    )

    interface ClickListener {
        fun onActionButtonPressed()
        fun onPrintButtonPressed()
    }

    private val binding: ComponentBottomAppBarBinding
    private var isPrinterAvailable: Boolean = false

    init {
        val view =
            LayoutInflater.from(context).inflate(R.layout.component_bottom_app_bar, this, true)
        binding = ComponentBottomAppBarBinding.bind(view)
        orientation = VERTICAL
    }

    fun setActionButtonText(textId: Int) {
        binding.actionButton.text = context.getString(textId)
    }

    fun setTransactionTypeText(textId: Int) {
        binding.transactionTypeText.text = context.getString(textId)
        binding.transactionTypeText.visibility = View.VISIBLE
        binding.printButton.visibility = View.GONE
    }

    fun setPrintButtonText(textId: Int) {
        binding.printButton.text = context.getString(textId)
        binding.printButton.visibility = View.VISIBLE
        binding.transactionTypeText.visibility = View.GONE
    }

    fun clearPrintButtonText() {
        binding.printButton.text = ""
    }

    fun enableActionButton() {
        binding.actionButton.isEnabled = true
        binding.actionButton.alpha = 1f
    }

    fun disableActionButton() {
        binding.actionButton.isEnabled = false
        binding.actionButton.alpha = .25f
    }

    fun hideTransactionTypeText() {
        binding.transactionTypeText.visibility = View.GONE
    }

    fun hidePrintButton() {
        binding.printButton.visibility = View.GONE
    }

    fun setButtonsListeners(listeners: ClickListener) {
        binding.printButton.setOnClickListener {
            listeners.onPrintButtonPressed()
        }
        binding.actionButton.setOnClickListener {
            listeners.onActionButtonPressed()
        }
    }

    fun setIconsForPrint() {
        val printDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_printer)
        binding.printButton.setCompoundDrawablesWithIntrinsicBounds(printDrawable, null, null, null)
        binding.printButton.visibility = if (isPrinterAvailable) View.VISIBLE else View.GONE
        val closeDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_next)
        binding.actionButton.setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            closeDrawable,
            null
        )
        binding.transactionTypeText.visibility = View.GONE
    }

    fun setupPrinterButtonVisibility(printerAvailable: Boolean) {
        isPrinterAvailable = printerAvailable
        binding.transactionTypeText.visibility = View.GONE
    }
}