package com.cm.payplaza.ecr_sdk_integration.fragment.receiptView

enum class AvailableReceipts(val value: Int) {
    NONE(0),
    CUSTOMER(1),
    MERCHANT(2),
    CUSTOMERANDMERCHANT(3);

    companion object {
        private val VALUES = values()
        fun getByValue(value: Int) = VALUES.firstOrNull { it.value == value }
    }
}