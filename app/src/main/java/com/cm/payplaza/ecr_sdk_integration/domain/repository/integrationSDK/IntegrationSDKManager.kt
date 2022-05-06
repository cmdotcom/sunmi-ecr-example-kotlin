package com.cm.payplaza.ecr_sdk_integration.domain.repository.integrationSDK

import com.cm.payplaza.ecr_sdk_integration.entity.sdkEntity.SDKResponse
import com.cm.androidposintegration.beans.DayTotalsOptions
import com.cm.androidposintegration.beans.LastReceiptOptions
import com.cm.androidposintegration.beans.RequestStatusData
import com.cm.androidposintegration.beans.TransactionData
import com.cm.androidposintegration.beans.PreAuthFinishData

interface IntegrationSDKManager {
    interface IntegrationSDKCallback {
        fun returnResponse(sdkResponse: SDKResponse)
    }
    fun doTransaction(transactionData: TransactionData, callback: IntegrationSDKCallback)
    fun doStatuses(requestStatusData: RequestStatusData, callback: IntegrationSDKCallback)
    fun doLastReceipt(lastReceiptOptions: LastReceiptOptions, callback: IntegrationSDKCallback)
    fun doRequireInfo(callback: IntegrationSDKCallback)
    fun doTotals(dayTotalsOptions: DayTotalsOptions, callback: IntegrationSDKCallback)
    fun doFinishPreauth(preAuthFinishData: PreAuthFinishData, callback: IntegrationSDKCallback)
}