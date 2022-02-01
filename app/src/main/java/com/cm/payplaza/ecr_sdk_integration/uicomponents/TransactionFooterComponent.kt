package com.cm.payplaza.ecr_sdk_integration.uicomponents

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import com.cm.androidposintegration.enums.TransactionType
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.databinding.ComponentTransactionFooterBinding
import java.math.BigDecimal

class TransactionFooterComponent: LinearLayoutCompat {

    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr)

    private var binding :ComponentTransactionFooterBinding

    init{
        val view = LayoutInflater.from(context).inflate(R.layout.component_transaction_footer, this, true)
        binding = ComponentTransactionFooterBinding.bind(view)
        orientation = VERTICAL
    }

    fun setAmount(currency: String, amount: BigDecimal) {
        ("Total amount $currency$amount").also { binding.labelTransactionAmount.text = it }
    }

    fun setTransactionMethod(type: TransactionType) {
        ("$type with card").also { binding.labelTransactionMethod}
    }
}