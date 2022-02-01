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
import timber.log.Timber

class ReceiptTextComponent: LinearLayoutCompat {
    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr)

    private var binding :ComponentReceiptTextBinding

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.component_receipt_text, this, true)
        binding = ComponentReceiptTextBinding.bind(view)
        orientation = VERTICAL
    }

    fun setReceiptData(receipt: Receipt?) {
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
            }
        }
    }
}