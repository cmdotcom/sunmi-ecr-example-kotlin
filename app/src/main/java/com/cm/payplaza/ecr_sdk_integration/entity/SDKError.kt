package com.cm.payplaza.ecr_sdk_integration.entity

import com.cm.payplaza.ecr_sdk_integration.R

class SDKError {
    companion object {
        val map = mapOf(
            0 to R.string.error_no_error,
            -1 to R.string.error_occurred,
            -2 to R.string.error_amount_invalid,
            -17 to R.string.error_printer_init_fail,
            -18 to R.string.error_pos_not_configured,
            -24 to R.string.error_bad_timezone,
            -23 to R.string.error_auto_timezone_not_enabled,
            -25 to R.string.error_host_not_connected,
            -30 to R.string.error_timeout,
            -31 to R.string.error_repeated_operation,
            -32 to R.string.error_merchant_order_ref_too_long,
            -33 to R.string.error_amount_limit_exceeded,
            -36 to R.string.error_low_battery,
            -50 to R.string.error_transaction_status_error
        )
    }
}