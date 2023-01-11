package com.cm.payplaza.ecr_sdk_integration.domain.repository.integrationSDK

import com.cm.androidposintegration.beans.*
import com.cm.androidposintegration.service.PosIntegrationService
import com.cm.payplaza.ecr_sdk_integration.BuildConfig
import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import timber.log.Timber

class IntegrationSDKMAnagerImpl(
    private val localDataRepository: LocalDataRepository,
    private val payplazaIntegrationService: PosIntegrationService?
): IntegrationSDKManager {
    override fun doTransaction(
        transactionData: TransactionData,
        callback: IntegrationSDKManager.IntegrationSDKCallback) {
        val transactionCallback = DoTransactionCallbackImpl(localDataRepository, callback)
        payplazaIntegrationService?.doTransaction(transactionData, transactionCallback)
    }

    override fun doStatuses(
        requestStatusData: RequestStatusData,
        callback: IntegrationSDKManager.IntegrationSDKCallback) {
        val statusesCallback = StatusesCallbackImpl(localDataRepository, callback)
        payplazaIntegrationService?.transactionStatuses(requestStatusData, statusesCallback)
    }

    override fun doLastReceipt(
        lastReceiptOptions: LastReceiptOptions,
        callback: IntegrationSDKManager.IntegrationSDKCallback) {
        val lastReceiptCallback = LastReceiptCallbackImpl(localDataRepository, callback)
        payplazaIntegrationService?.getLastReceipt(lastReceiptOptions, lastReceiptCallback)
    }

    override fun doRequireInfo(callback: IntegrationSDKManager.IntegrationSDKCallback) {
        val terminalInfoCallback = TerminalInfoCallbackImpl(localDataRepository, callback)
        payplazaIntegrationService?.getTerminalInfo(terminalInfoCallback)
    }

    override fun doTotals(
        dayTotalsOptions: DayTotalsOptions,
        callback: IntegrationSDKManager.IntegrationSDKCallback) {
        val totalsCallback = TotalsCallbackImpl(localDataRepository, callback)
        payplazaIntegrationService?.getTerminalDayTotals(dayTotalsOptions, totalsCallback)
    }

    override fun doFinishPreauth(
        preAuthFinishData: PreAuthFinishData,
        callback: IntegrationSDKManager.IntegrationSDKCallback) {
        val transactionCallback = DoTransactionCallbackImpl(localDataRepository, callback)
        payplazaIntegrationService?.finishPreAuth(preAuthFinishData, transactionCallback)
    }
}