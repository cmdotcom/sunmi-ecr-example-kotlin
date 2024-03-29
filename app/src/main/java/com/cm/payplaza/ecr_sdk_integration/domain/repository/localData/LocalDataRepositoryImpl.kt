package com.cm.payplaza.ecr_sdk_integration.domain.repository.localData

import com.cm.payplaza.ecr_sdk_integration.domain.repository.storedData.StoredDataRepository
import com.cm.payplaza.ecr_sdk_integration.entity.*
import org.koin.core.component.KoinComponent


class LocalDataRepositoryImpl(private val storedDataRepository: StoredDataRepository) : LocalDataRepository, KoinComponent {

    companion object {
        private var transaction: Transaction? = null
    }

    private var transactionError: TransactionError? = null
    private var orderReference: Int = storedDataRepository.loadOrderReference()
    private var transactionResult: TransactionResponse? = null
    private var terminalData: TerminalData? = null
    private var statuses: StatusesData? = null

    override fun getTransaction(): Transaction? = transaction

    override fun getTransactionError(): TransactionError? = this.transactionError
    override fun getOrderReference(): Int = this.orderReference
    override fun getTransactionResponse(): TransactionResponse? = this.transactionResult
    override fun getTerminalData(): TerminalData? = this.terminalData
    override fun getStatusesData(): StatusesData? = this.statuses

    override fun setTransaction(transaction: Transaction) {
        LocalDataRepositoryImpl.transaction = transaction
    }

    override fun setTransactionResponse(result: TransactionResponse) { this.transactionResult = result }
    override fun setTerminalData(terminalData: TerminalData) { this.terminalData = terminalData }
    override fun setStatusesData(statusesData: StatusesData) { this.statuses = statusesData }

    override fun setTransactionError(transactionError: TransactionError) { this.transactionError = transactionError }

    override fun clearTransactionData() {
        transactionError = null
        transactionResult = null
    }

    override fun increaseOrderReference() {
        val currentOrderReference = storedDataRepository.loadOrderReference()
        val newOrderReference = currentOrderReference + 1
        this.orderReference = newOrderReference
        storedDataRepository.saveOrderReference(this.orderReference)
    }
}