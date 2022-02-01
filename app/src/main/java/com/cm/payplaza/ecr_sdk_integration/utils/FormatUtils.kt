package com.cm.payplaza.ecr_sdk_integration.utils

import android.util.Log
import com.cm.payplaza.ecr_sdk_integration.BuildConfig
import com.cm.payplaza.ecr_sdk_integration.entity.Receipt
import java.lang.StringBuilder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class FormatUtils {
    companion object {
        fun formatAmount(amount: Int): String {
            val doubleAmount: Double = (amount.toDouble()) / 100
            return "%.2f".format(doubleAmount).replace(",", ".")
        }
        fun formatDateForDateView(date: Date): String {
            val formatter = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
            return formatter.format(date)
        }
        fun getDateFromLines(receiptLines: Array<String>): Date {
            val formatter = SimpleDateFormat("dd-MM-yy__HH:mm:ss", Locale.getDefault())
            var date = Date()
            receiptLines.forEach {
                val index = it.indexOf(":")
                if(index >= 0) {
                    var line = it.substring(index + 1)
                    line = line.replace(" ", "_")
                    try {
                        val receiptDate = formatter.parse(line) ?: run { Date() }
                        date = receiptDate
                    } catch(e: Exception) { }
                }
            }
            return date
        }
        fun getDateFromReceipt(receipt: Receipt): Date {
            var date = Date()

            receipt.receiptLines?.let {
                val receiptDate = FormatUtils.getDateFromLines(it)
                date = receiptDate
            }

            return date
        }
    }
}