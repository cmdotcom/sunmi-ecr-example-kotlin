package com.cm.payplaza.ecr_sdk_integration.utils.printer

class PrinterFonts {
    data class FontWidth(val minWidth: Int, val maxWidth: Int, val fontSize: Int)
    companion object {
        val fonts = arrayOf(
            FontWidth(Integer.MIN_VALUE, 19, 40),
            FontWidth(20, 20, 38),
            FontWidth(21, 21, 36),
            FontWidth(22, 22, 34),
            FontWidth(23, 24, 32),
            FontWidth(25, 25, 30),
            FontWidth(26, 27, 28),
            FontWidth(28, 29, 26),
            FontWidth(30, 32, 24),
            FontWidth(33, 34, 22),
            FontWidth(35, 38, 20),
            FontWidth(39, 42, 18),
            FontWidth(43, 48, 16),
            FontWidth(49, 54, 14),
            FontWidth(55, Integer.MAX_VALUE, 12)
        )
    }
}