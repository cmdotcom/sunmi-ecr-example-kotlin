package com.cm.payplaza.ecr_sdk_integration.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

object BigDecimalUtil {
    fun toLong(bigDecimal: BigDecimal, currency: Currency): Long {
        return toLong(bigDecimal, currency.defaultFractionDigits)
    }

    fun toLong(bigDecimal: BigDecimal, precision: Int): Long {
        return bigDecimal.setScale(precision, RoundingMode.HALF_EVEN).movePointRight(precision)
            .toLong()
    }

    @JvmStatic
    fun fromLong(amount: Long?, currencyCode: Int): BigDecimal? {
        if (amount != null) {
            val currency = getCurrencyInstance(currencyCode)
            return BigDecimal(amount).movePointLeft(currency.defaultFractionDigits)
        }
        return null
    }

    fun fromInt(amount: Int, currencyCode: Int): BigDecimal {
        val currency = getCurrencyInstance(currencyCode)
        return BigDecimal(amount).movePointLeft(currency.defaultFractionDigits)
    }

    fun getCurrencyInstance(numericCode: Int): Currency {
        val currencies = Currency.getAvailableCurrencies()
        for (currency in currencies) {
            if (currency.numericCode == numericCode) {
                return currency
            }
        }
        val e = IllegalArgumentException("Currency with numeric code $numericCode not found")
        throw e
    }
}