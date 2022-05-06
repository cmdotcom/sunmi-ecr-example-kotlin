package com.cm.payplaza.ecr_sdk_integration.koin

import com.cm.androidposintegration.initializer.AndroidPosIntegration
import com.cm.androidposintegration.service.PosIntegrationService
import com.cm.payplaza.ecr_sdk_integration.domain.repository.integrationSDK.IntegrationSDKMAnagerImpl
import com.cm.payplaza.ecr_sdk_integration.domain.repository.integrationSDK.IntegrationSDKManager
import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepositoryImpl
import com.cm.payplaza.ecr_sdk_integration.domain.repository.storedData.StoredDataRepository
import com.cm.payplaza.ecr_sdk_integration.domain.repository.storedData.StoredDataRepositoryImpl
import com.cm.payplaza.ecr_sdk_integration.utils.printer.SunmiPrinter
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val repositoryModule = module {
    single<LocalDataRepository> { LocalDataRepositoryImpl(get()) }
    single<StoredDataRepository> { StoredDataRepositoryImpl(get()) }
    single<PosIntegrationService?> { AndroidPosIntegration.getInstance() }
    single<IntegrationSDKManager> { IntegrationSDKMAnagerImpl(get(), get()) }
    single { SunmiPrinter(androidApplication()) }
    single {
        val prefsName = "order_reference"
        androidApplication().getSharedPreferences(prefsName, 0)
    }
}