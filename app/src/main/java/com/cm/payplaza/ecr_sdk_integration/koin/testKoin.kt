package com.cm.payplaza.ecr_sdk_integration.koin

import com.cm.androidposintegration.beans.*
import com.cm.payplaza.ecr_sdk_integration.entity.*
import com.cm.androidposintegration.enums.TransactionResult
import com.cm.androidposintegration.enums.TransactionType
import com.cm.androidposintegration.service.PosIntegrationService
import com.cm.androidposintegration.service.callback.ReceiptCallback
import com.cm.androidposintegration.service.callback.StatusesCallback
import com.cm.androidposintegration.service.callback.TerminalInfoCallback
import com.cm.androidposintegration.service.callback.TransactionCallback
import com.cm.androidposintegration.service.callback.beans.LastReceiptResultData
import com.cm.androidposintegration.service.callback.beans.TerminalInfoData
import com.cm.androidposintegration.service.callback.beans.TransactionResultData
import com.cm.androidposintegration.service.callback.beans.TransactionStatusesData
import com.cm.androidposintegration.service.callback.json.TransactionStatusData
import com.cm.payplaza.ecr_sdk_integration.R
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
            get(named("testMerchantReceiptData")),
            BigDecimal.ZERO,
            BigDecimal.ZERO,
        )
    }

    factory(named("transactionError")) {
        TransactionError(
            R.string.error_occurred,
            -1)
    }

    factory(named("customerReceiptLines")) {
        arrayOf(
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
        )
    }

    factory(named("merchantReceiptLines")) {
        arrayOf(
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

    factory(named("testMerchantReceipt")) {
        ReceiptData(get(qualifier = named("merchantReceiptLines")), null)
    }

    factory(named("testCustomerReceipt")) {
        ReceiptData(get(qualifier = named("customerReceiptLines")), null)
    }

    factory(named("mockPOSIntegration")) {
        object : PosIntegrationService {
            override fun doTransaction(data: TransactionData, callback: TransactionCallback) {
                val transactionResultData = TransactionResultData(
                    TransactionResult.SUCCESS,
                    "1",
                    BigDecimal(100),
                    "00",
                    "CARD_ENTRY_MODE_CONTACTLESS",
                    "1234",
                    "Omnipay",
                    Date(),
                    "0001012021110811122695300000001",
                    "Mastercard",
                    "A0000000041010",
                    "54673892084574",
                    4,
                    "100364",
                    get(qualifier = named("testMerchantReceipt")),
                    get(qualifier = named("testCustomerReceipt")),
                    BigDecimal(10)
                )
                callback.onResult(transactionResultData)
            }

            override fun finishPreAuth(data: PreAuthFinishData, callback: TransactionCallback) {
                val transactionResultData = TransactionResultData(
                    TransactionResult.SUCCESS,
                    "1",
                    BigDecimal(100),
                    "00",
                    "CARD_ENTRY_MODE_CONTACTLESS",
                    "1234",
                    "Omnipay",
                    Date(),
                    "0001012021110811122695300000001",
                    "Mastercard",
                    "A0000000041010",
                    "54673892084574",
                    4,
                    "100364",
                    get(qualifier = named("testMerchantReceipt")),
                    get(qualifier = named("testCustomerReceipt")),
                    BigDecimal(10)
                )
                callback.onResult(transactionResultData)
            }

            override fun getLastReceipt(options: LastReceiptOptions, callback: ReceiptCallback) {
                val lastReceiptResultData = LastReceiptResultData(get(qualifier = named("testMerchantReceipt")))
                callback.onResult(lastReceiptResultData)
            }

            override fun getTerminalDayTotals(
                options: DayTotalsOptions,
                callback: ReceiptCallback
            ) {
                val lastReceiptResultData = LastReceiptResultData(get(qualifier = named("testMerchantReceipt")))
                callback.onResult(lastReceiptResultData)
            }

            override fun getTerminalInfo(callback: TerminalInfoCallback) {
                val currency = Currency.getInstance("EUR")
                val terminalInfoData = TerminalInfoData(
                    TransactionResult.SUCCESS,
                    "Edu & David's",
                    "Av. Castellana 79",
                    "Madrid",
                    "28046",
                    "es",
                    "es",
                    currency,
                    "P211224H0005",
                    "2.0.0"
                )
                callback.onResult(terminalInfoData)
            }

            override fun transactionStatuses(data: RequestStatusData, callback: StatusesCallback) {
                val currency = Currency.getInstance("EUR")
                val status1 = TransactionStatusData(
                    BigDecimal(100),
                    currency,
                    TransactionResult.SUCCESS,
                    TransactionType.PURCHASE,
                    get(qualifier = named("testMerchantReceipt"))
                )
                val status2 = TransactionStatusData(
                    BigDecimal(100),
                    currency,
                    TransactionResult.AUTHORIZATION_TIMEOUT,
                    TransactionType.REFUND,
                    get(qualifier = named("testMerchantReceipt"))
                )

                val statusesInfo: List<TransactionStatusData> = listOf(status1, status2)
                val transactionStatusesData = TransactionStatusesData(
                    statusesInfo,
                    "No error",
                    2
                )
                callback.onResult(transactionStatusesData)
            }

        }
    }
}