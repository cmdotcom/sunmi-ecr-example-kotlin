package com.cm.payplaza.ecr_sdk_integration.domain.repository.integrationSDK

import com.cm.androidposintegration.beans.DayTotalsOptions
import com.cm.androidposintegration.beans.LastReceiptOptions
import com.cm.androidposintegration.beans.RequestStatusData
import com.cm.androidposintegration.beans.TransactionData
import com.cm.androidposintegration.service.PosIntegrationService
import com.cm.androidposintegration.service.callback.StatusesCallback
import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository

class IntegrationSDKMAnagerImpl(
    private val localDataRepository: LocalDataRepository,
    private val posIntegrationService: PosIntegrationService?
): IntegrationSDKManager {
    override fun doTransaction(
        transactionData: TransactionData,
        callback: IntegrationSDKManager.IntegrationSDKCallback) {
        val transactionCallback = DoTransactionCallbackImpl(localDataRepository, callback)
        posIntegrationService?.doTransaction(transactionData, transactionCallback)
    }

    override fun doStatuses(
        requestStatusData: RequestStatusData,
        callback: IntegrationSDKManager.IntegrationSDKCallback) {
        val statusesCallback = StatusesCallbackImpl(localDataRepository, callback)
        posIntegrationService?.transactionStatuses(requestStatusData, statusesCallback)
    }

    override fun doLastReceipt(
        lastReceiptOptions: LastReceiptOptions,
        callback: IntegrationSDKManager.IntegrationSDKCallback) {
        val lastReceiptCallback = LastReceiptCallbackImpl(localDataRepository, callback)
        posIntegrationService?.getLastReceipt(lastReceiptOptions, lastReceiptCallback)
    }

    override fun doRequireInfo(callback: IntegrationSDKManager.IntegrationSDKCallback) {
        val terminalInfoCallback = TerminalInfoCallbackImpl(localDataRepository, callback)
        posIntegrationService?.getTerminalInfo(terminalInfoCallback)
    }

    override fun doTotals(
        dayTotalsOptions: DayTotalsOptions,
        callback: IntegrationSDKManager.IntegrationSDKCallback) {
        val totalsCallback = TotalsCallbackImpl(localDataRepository, callback)
        posIntegrationService?.getTerminalDayTotals(dayTotalsOptions, totalsCallback)
    }
}