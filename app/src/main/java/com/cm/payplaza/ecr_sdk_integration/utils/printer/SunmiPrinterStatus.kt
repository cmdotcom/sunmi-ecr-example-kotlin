package com.cm.payplaza.ecr_sdk_integration.utils.printer

enum class SunmiPrinterStatus(val value: Int) {
        NORMAL(1),
        UPDATE_STATUS(2),
        GET_STATUS_EXCEPTION(3),
        OUT_OF_PAPER(4),
        OVERHEATED(5),
        TRAY_OPEN(6),
        CUTTER_ABNORMAL(7),
        CUTTER_RECOVERY(8),
        NO_BLACK_MARK(9),
        NO_PRINTER_DETECTED(10);

        companion object {
                private val VALUES = values()
                fun getByValue(value: Int) = VALUES.firstOrNull { it.value == value }
        }

}