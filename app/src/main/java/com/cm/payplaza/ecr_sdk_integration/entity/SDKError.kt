package com.cm.payplaza.ecr_sdk_integration.entity

class SDKError {
    companion object {
        val map = mapOf(
            0 to "No error",
            -1 to "Unknown error",
            -2 to "Amount used was invalid",
            -17 to "Printer initialization failed",
            -18 to "Device is not configured in gateway",
            -24 to "Timezone on the device is not correct",
            -25 to "Cannot connect with the gateway",
            -30 to "Timeout reaching the gateway",
            -31 to "Transaction already in progress",
            -32 to "Order ref exceeds allowed length",
            -33 to "Transaction exceeds allowed limit",
            -50 to "Status information not received"
        )
    }
}