package com.cm.payplaza.ecr_sdk_integration.activity.preauth.finish

import androidx.lifecycle.viewModelScope
import com.cm.androidposintegration.beans.PreAuthFinishData
import com.cm.androidposintegration.enums.PreAuthFinishType
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivityViewModel
import com.cm.payplaza.ecr_sdk_integration.activity.preauth.navigationMenu.PreauthType
import com.cm.payplaza.ecr_sdk_integration.domain.repository.integrationSDK.IntegrationSDKManager
import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.entity.sdkEntity.SDKResponse
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import java.util.*

class FinishPreauthViewModel: BaseEcrFragmentActivityViewModel() {

    private val integrationSDKManager: IntegrationSDKManager by inject()
    private val localDataRepository: LocalDataRepository by inject()

    private var _preauthAmount = 0
    private lateinit var _preauthStan: String
    private lateinit var _preauthDate: Date
    private lateinit var _preauthType: PreauthType

    fun saveAmount(amount: Int) {
        this._preauthAmount = amount
    }

    fun saveStan(stan: String) {
        this._preauthStan = stan
    }

    fun saveDate(date: Date) {
        this._preauthDate = date
    }

    fun doPreauth() {
        val amount = (_preauthAmount.toDouble() / 100).toBigDecimal()
        val merchantData = localDataRepository.getTerminalData()
        val currency = merchantData?.currency ?: run {
            Currency.getInstance(Locale.getDefault())
        }
        val orderReference = localDataRepository.getOrderReference()
        val preauthSDKType = if(_preauthType == PreauthType.CONFIRM) {
            PreAuthFinishType.SALE_AFTER_PRE_AUTH
        } else {
            PreAuthFinishType.CANCEL_PRE_AUTH
        }
        val preauthFinishData = PreAuthFinishData(
            preauthSDKType,
            _preauthStan,
            _preauthDate,
            orderReference.toString()
        )
        preauthFinishData.isShowReceipt = false
        if(_preauthType == PreauthType.CONFIRM) {
            preauthFinishData.amount = amount
            preauthFinishData.currency = currency
        }
        val callback = object : (IntegrationSDKManager.IntegrationSDKCallback) {
            override fun returnResponse(sdkResponse: SDKResponse) {
                when (sdkResponse) {
                    SDKResponse.ON_RESULT -> updateView(FinishPreauthViewState.OnResult)
                    SDKResponse.ON_ERROR -> updateView(FinishPreauthViewState.OnError)
                    SDKResponse.ON_CRASH -> updateView(FinishPreauthViewState.OnCrash)
                }
            }
        }
        viewModelScope.launch {
            integrationSDKManager.doFinishPreauth(preauthFinishData, callback)
        }
    }

    fun savePreauthType(preauthType: PreauthType) {
        _preauthType = preauthType
    }
}