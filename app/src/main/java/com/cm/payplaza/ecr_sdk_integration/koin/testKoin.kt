package com.cm.payplaza.ecr_sdk_integration.koin

import com.cm.payplaza.ecr_sdk_integration.entity.*
import com.cm.androidposintegration.enums.TransactionResult
import com.cm.androidposintegration.enums.TransactionType
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.math.BigDecimal
import java.util.*

val testModule = module {

    factory(named("purchaseTransaction")) {
        Transaction(
            BigDecimal(13.24),
            Currency.getInstance("EUR"),
            TransactionType.PURCHASE,
            Date(),
            "123456")
    }

    factory(named("refundTransaction")) {
        Transaction(
            BigDecimal(13.24),
            Currency.getInstance("EUR"),
            TransactionType.REFUND,
            Date(),
            "123456")
    }

    factory(named("transactionResultSuccess")) {
        TransactionResponse(
            TransactionResult.SUCCESS,
            "",
            get(named("testCustomerReceiptData")),
            get(named("testMerchantReceiptData")))
    }

    factory(named("transactionError")) {
        TransactionError(
            "TestError",
            -1)
    }

    factory(named("customerReceiptLines")) {
        arrayOf(
            "Keep up the good work CUSTOMER",
            "Terminal: *****001",
            "Merchant: *****890",
            "ECR: E_SUNMI_P2_PRO",
            "STAN:       489693",
            "AID: A000000000041010"
        )
    }

    factory(named("merchantReceiptLines")) {
        arrayOf(
            "Keep up the good work MERCHANT",
            "Terminal: *****001",
            "Merchant: *****890",
            "ECR: E_SUNMI_P2_PRO",
            "STAN:       489693",
            "AID: A000000000041010"
        )
    }

    factory(named("merchantData")) {
        TerminalData(
            "PL09214500061",
            "Calle Alcalá 1",
            "Madrid",
            "España",
            "ES",
            "Casa Julián",
            "28001",
            TransactionResult.SUCCESS,
            "1.0.42",
            Currency.getInstance("EUR")
        )
    }

    factory(named("testCustomerReceiptData")) {
        Receipt(get(qualifier = named("customerReceiptLines")), null)
    }

    factory(named("testMerchantReceiptData")) {
        Receipt(get(qualifier = named("merchantReceiptLines")), null)
    }
}