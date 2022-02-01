package com.cm.payplaza.ecr_sdk_integration.activity.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cm.payplaza.ecr_sdk_integration.domain.repository.integrationSDK.IntegrationSDKManager
import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.entity.sdkEntity.SDKResponse
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

abstract class BaseEcrViewModel: ViewModel(),
    KoinComponent {

    protected val integrationSDKManager: IntegrationSDKManager by inject()
    protected val localDataRepository: LocalDataRepository by inject()

    private val _state = MutableLiveData<BaseEcrViewState>()
    val state : LiveData<BaseEcrViewState> get() = _state

    protected fun updateView(vs: BaseEcrViewState) {
        _state.value =  vs
    }

    fun requestInfo() {
        Timber.d("requestInfo")
        val callback = object: IntegrationSDKManager.IntegrationSDKCallback {
            override fun returnResponse(sdkResponse: SDKResponse) {
                when(sdkResponse) {
                    SDKResponse.ON_RESULT -> setUpData()
                    SDKResponse.ON_ERROR -> updateView(BaseEcrViewState.RequestInfoFailed)
                    SDKResponse.ON_CRASH -> updateView(BaseEcrViewState.RequestInfoFailed)
                }
            }
        }
        viewModelScope.launch {
            integrationSDKManager.doRequireInfo(callback)
        }
    }

    open fun init() = updateView(BaseEcrViewState.Init(localDataRepository.getTerminalData()))
    protected open fun setUpData() {
        val terminalData = localDataRepository.getTerminalData()
        updateView(BaseEcrViewState.RequestInfo(terminalData))
    }
}