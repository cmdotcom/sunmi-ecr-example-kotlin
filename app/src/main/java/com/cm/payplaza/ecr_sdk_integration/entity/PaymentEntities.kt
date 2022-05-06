package com.cm.payplaza.ecr_sdk_integration.entity

import com.cm.payplaza.ecr_sdk_integration.utils.FormatUtils
import com.cm.androidposintegration.enums.TransactionResult
import com.cm.androidposintegration.enums.TransactionType
import com.cm.androidposintegration.service.callback.beans.TerminalInfoData
import com.cm.androidposintegration.service.callback.beans.TransactionStatusesData
import com.cm.androidposintegration.service.callback.json.TransactionStatusData
import java.math.BigDecimal
import java.util.*
import java.util.regex.Pattern

open class Entity
data class Transaction(
    val amount: BigDecimal,
    val currency: Currency,
    val transactionType: TransactionType,
    val refundDate: Date?,
    val refundStan: String?
): Entity()
data class TransactionError(
    val desc: String,
    val value: Int
): Entity()
data class Receipt(
    val receiptLines: Array<String>?,
    val signature: ByteArray?
): Entity() {
    companion object {
        private val DOTTED_LINE_PATTERN = Pattern.compile("^\\.{4,}$")
        private fun isDotLine(line: String): Boolean = DOTTED_LINE_PATTERN.matcher(line).matches()
        fun empty() = Receipt(arrayOf(), ByteArray(0))
    }
    fun isEmpty(): Boolean = receiptLines?.isEmpty() ?: run { true }
    fun getLinesBeforeSignature(): Array<String> {
        var lines: Array<String> = arrayOf()
        this.signature?.let {
            this.receiptLines?.let {
                it.forEach { line ->
                    if(isDotLine(line)) {
                        lines = it.copyOfRange(0, it.indexOf(line) - 1)
                    }
                }
            }
        } ?: run {
            lines = this.receiptLines ?: run { arrayOf() }
        }
        return lines
    }

    fun getLinesAfterSignature(): Array<String> {
        var lines: Array<String> = arrayOf()
        this.signature?.let {
            this.receiptLines?.let {
                it.forEach { line ->
                    if(isDotLine(line)) {
                        lines = it.copyOfRange(it.indexOf(line), it.size)
                    }
                }
            }
        } ?: run {
            lines = this.receiptLines ?: run { arrayOf() }
        }
        return lines
    }
}
data class TransactionResponse(
    val result: TransactionResult,
    val orderReference: String,
    val customerReceipt: Receipt,
    val merchantReceipt: Receipt?
): Entity()
data class TerminalData(
    val deviceSerialNumber: String?,
    val storeAddress: String?,
    val storeCity: String?,
    val storeCountry: String?,
    val storeLanguage: String?,
    val storeName: String?,
    val storeZipCode: String?,
    val transactionResult: TransactionResult?,
    val versionNumber: String?,
    val currency: Currency?
): Entity() {
    companion object {
        fun toTerminalData(data: TerminalInfoData): TerminalData {
            return TerminalData(
                data.deviceSerialNumber,
                data.storeAddress,
                data.storeCity,
                data.storeCountry,
                data.storeLanguage,
                data.storeName,
                data.storeZipCode,
                data.transactionResult,
                data.versionNumber,
                data.storeCurrency
            )
        }
    }
}

data class StatusesData(
    val error: String,
    val count: Int,
    val data: List<StatusData>
): Entity() {
    companion object {
        fun toStatusesData(statuses: TransactionStatusesData): StatusesData {
            val statusData = arrayListOf<StatusData>()
            statuses.statusesInfo?.forEach {
                statusData.add(StatusData.toStatusData(it))
            }
            statusData.sortByDescending { it.date }
            return StatusesData(
                statuses.errorMessage,
                statuses.totalCount,
                statusData
            )
        }
    }
}

data class StatusData(
    val amount: BigDecimal,
    val currency: Currency,
    val receipt: Receipt,
    val result: TransactionResult,
    val type: TransactionType,
    val date: Date
): Entity(){
    companion object {
        fun toStatusData(data: TransactionStatusData): StatusData {
            val receipt: Receipt = data.receipt?.let {
                Receipt(it.receiptLines, it.signature)
            } ?: run { Receipt.empty() }
            val date = FormatUtils.getDateFromReceipt(receipt)
            return StatusData(
                data.amount,
                data.currency,
                receipt,
                data.result,
                data.type,
                date
            )
        }
    }
}