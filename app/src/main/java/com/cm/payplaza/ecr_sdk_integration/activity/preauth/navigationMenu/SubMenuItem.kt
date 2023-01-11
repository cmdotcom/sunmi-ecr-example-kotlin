package com.cm.payplaza.ecr_sdk_integration.activity.preauth.navigationMenu

import com.cm.payplaza.ecr_sdk_integration.R

data class SubMenuItem(
    val textId: Int,
    val title: String,
    val icon: Int,
    var isEnabled: Boolean = true
)

enum class MenuItem(val value: Int) {
    NEW_PAYMENT(0),
    PRE_AUTH(1),
    REFUND(2),
    PRINT_LAST_RECEIPT(3),
    DAY_TOTALS(4),
    DOWNLOAD_PARAMETERS(5),
    CANCEL_PAYMENT(6),
}

fun convert(item: MenuItem): Int {
    return when(item.name) {
        MenuItem.NEW_PAYMENT.name -> R.string.payment
        MenuItem.PRE_AUTH.name -> R.string.preauth
        MenuItem.REFUND.name -> R.string.refund
        MenuItem.PRINT_LAST_RECEIPT.name -> R.string.print_last_receipt
        MenuItem.DAY_TOTALS.name -> R.string.day_totals
        MenuItem.DOWNLOAD_PARAMETERS.name -> R.string.request_info
        MenuItem.CANCEL_PAYMENT.name -> R.string.cancel_payment

        else -> -1
    }
}