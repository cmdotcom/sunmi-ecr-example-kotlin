package com.cm.payplaza.ecr_sdk_integration

import com.cm.payplaza.ecr_sdk_integration.entity.Receipt
import com.cm.payplaza.ecr_sdk_integration.utils.FormatUtils
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class FormatUtilsUnitTest {

    private val testReceipt = Receipt(arrayOf(
        "      One Terminal      ",
        "",
        "Terminal:       *****001",
        "Merchant:       *****748",
        "STAN:             156136",
        "",
        "               CHIP READ",
        "AID:      A0000000031010",
        "          FALABELLA VISA",
        "Card:       ********2443",
        "Cardnr:                1",
        "",
        "Date: 25-04-22  17:56:06",
        "",
        "Processor:       OmniPay",
        "Auth. code:             ",
        "Auth. resp. code:     51",
        "",
        "AMOUNT:        EUR 20.02",
        "REF:                   2",
        "",
        "    Payment  Failed     ",
        "",
        "CARDHOLDER RECEIPT      ",
        "",
        "",
        "To rule them all        "
    ), null)

    @Before
    fun initTest() {}

    // formatAmount
    @Test
    fun formatUtils_FormatAmount_zeroAmount_zeroExponent_Test() {
        val amount = 0
        val currencyExponent = 0
        val formattedAmount = FormatUtils().formatAmount(amount, currencyExponent)
        Assert.assertTrue("0".equals(formattedAmount, true))
    }
    @Test
    fun formatUtils_FormatAmount_zeroAmount_twoExponent_Test() {
        val amount = 0
        val currencyExponent = 2
        val formattedAmount = FormatUtils().formatAmount(amount, currencyExponent)
        Assert.assertTrue("0.00".equals(formattedAmount, true))
    }
    @Test
    fun formatUtils_FormatAmount_zeroDecimal_Test() {
        val amount = 123456
        val currencyExponent = 0
        val formattedAmount = FormatUtils().formatAmount(amount, currencyExponent)
        Assert.assertTrue("123456".equals(formattedAmount, true))
    }
    @Test
    fun formatUtils_FormatAmount_oneDecimal_Test() {
        val amount = 123456
        val currencyExponent = 1
        val formattedAmount = FormatUtils().formatAmount(amount, currencyExponent)
        Assert.assertTrue("12345.6".equals(formattedAmount, true))
    }
    @Test
    fun formatUtils_FormatAmount_TwoDecimals_Test() {
        val amount = 123456
        val currencyExponent = 2
        val formattedAmount = FormatUtils().formatAmount(amount, currencyExponent)
        Assert.assertTrue("1234.56".equals(formattedAmount, true))
    }
    @Test
    fun formatUtils_FormatAmount_threeDecimals_Test() {
        val amount = 123456
        val currencyExponent = 3
        val formattedAmount = FormatUtils().formatAmount(amount, currencyExponent)
        Assert.assertTrue("123.456".equals(formattedAmount, true))
    }

    // formatDateForDateView
    @Test
    fun formatUtils_formatDateForDateView_Test() {
        val cal = Calendar.getInstance()
        val time = cal.time
        val formattedDate = FormatUtils().formatDateForDateView(time)
        val formatter = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        val testFormattedDate = formatter.format(time)
        Assert.assertTrue(testFormattedDate.equals(formattedDate, true))
    }

    // getDateFromReceipt
    // getDateFromLines
    @Test
    fun formatUtils_getDateFromReceipt_Test() { // 25-04-22
        val dateFromReceiptLines = FormatUtils().getDateFromReceipt(testReceipt)
        val formatter = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
        val formattedDate = formatter.format(dateFromReceiptLines)
        Assert.assertTrue("25-04-22".equals(formattedDate, true))
    }

}