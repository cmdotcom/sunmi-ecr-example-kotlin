package com.cm.payplaza.ecr_sdk_integration.uicomponents

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.databinding.ComponentReceiptTextBinding
import com.cm.payplaza.ecr_sdk_integration.entity.Receipt

class ReceiptTextComponent: LinearLayoutCompat {
    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr)

    private var binding: ComponentReceiptTextBinding

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.component_receipt_text, this, true)
        binding = ComponentReceiptTextBinding.bind(view)
        orientation = VERTICAL
    }

    fun setReceiptData(receipt: Receipt?, showDayTotalReceipt: Boolean) {
        setPadding(showDayTotalReceipt)

        val str = StringBuilder()
        receipt?.let {
            // Setup text info
            val linesBeforeSignature = it.getLinesBeforeSignature()
            linesBeforeSignature.forEach { line -> str.appendLine(line) }
            binding.textviewReceipt.text = str.toString()
            it.signature?.let { signature ->
                // Setup signature
                val bmp: Bitmap = BitmapFactory.decodeByteArray(signature, 0, signature.size)
                binding.imageviewSignature.setImageBitmap(bmp)
                binding.imageviewSignature.visibility = View.VISIBLE
                str.clear()
                val linesAfterSignature = it.getLinesAfterSignature()
                linesAfterSignature.forEach { line -> str.appendLine(line) }
                binding.textviewReceiptFooter.visibility = View.VISIBLE
                binding.textviewReceiptFooter.text = str.toString()
            } ?: run {
                val bitmapConfig =  Bitmap.Config.ARGB_8888
                binding.imageviewSignature.setImageBitmap(Bitmap.createBitmap(1,1,bitmapConfig))
                binding.imageviewSignature.visibility = View.INVISIBLE
                binding.textviewReceiptFooter.visibility = View.INVISIBLE
                binding.textviewReceiptFooter.text = ""
            }
        }
    }

    private fun setPadding(showDayTotalReceipt: Boolean) {
        val padding: Int = if (showDayTotalReceipt) {
            resources.getDimensionPixelSize(R.dimen.padding_extra_small)
        } else {
            resources.getDimensionPixelSize(R.dimen.padding_extra_large)
        }
        binding.layoutReceipt.setPadding(padding, padding, padding, padding)
    }
}