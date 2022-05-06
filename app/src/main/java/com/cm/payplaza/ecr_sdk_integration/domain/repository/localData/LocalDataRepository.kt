package com.cm.payplaza.ecr_sdk_integration.domain.repository.localData

import com.cm.payplaza.ecr_sdk_integration.entity.*

interface LocalDataRepository {
    fun setTransaction(transaction: Transaction)
    fun getTransaction() : Transaction?

    fun setTransactionError(transactionError: TransactionError)
    fun getTransactionError(): TransactionError?

    fun clearTransactionData()

    fun getOrderReference(): Int
    fun increaseOrderReference()

    fun getTransactionResponse(): TransactionResponse?
    fun setTransactionResponse(result: TransactionResponse)

    fun getTerminalData(): TerminalData?
    fun setTerminalData(terminalData: TerminalData)

    fun getStatusesData(): StatusesData?
    fun setStatusesData(statusesData: StatusesData)
}