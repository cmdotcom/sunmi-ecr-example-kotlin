package com.cm.payplaza.ecr_sdk_integration.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class AmountFormatHelper private constructor() {

    init {
        throw UnsupportedOperationException("This is a utility class and cannot be instantiated")
    }

    companion object {

        fun formatAmount(units: Long, decimalPlaces: Int, decimalSeparator: String?): String {
            var amountString: String

            if (decimalPlaces > 0) {
                val format = String.format(Locale.US, "%%d.%%0%dd", decimalPlaces)
                var exponent: Long = 1
                for (i in 0 until decimalPlaces) {
                    exponent *= 10
                }
                val amount = units / exponent
                val remainder = units % exponent
                amountString = String.format(Locale.US, format, amount, remainder)
                amountString = amountString.replace(".", decimalSeparator!!)
            } else {
                amountString = String.format(Locale.US, "%d", units)
            }

            return amountString
        }

        fun formatCurrencyAmount(units: Long, currencyCode: String?, locale: Locale?): String {
            val currency = Currency.getInstance(currencyCode)
            val format = NumberFormat.getCurrencyInstance(locale)
            val df = format as DecimalFormat
            val fractionalAmount =
                units.toDouble() / Math.pow(10.0, currency.defaultFractionDigits.toDouble())
            format.setCurrency(currency)
            format.setMinimumFractionDigits(currency.defaultFractionDigits)
            format.setMaximumFractionDigits(currency.defaultFractionDigits)

            return df.format(fractionalAmount)
        }
    }
}