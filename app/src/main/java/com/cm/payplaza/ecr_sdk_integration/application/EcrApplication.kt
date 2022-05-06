package com.cm.payplaza.ecr_sdk_integration.application

import android.app.Application
import com.cm.androidposintegration.initializer.AndroidPosIntegration
import com.cm.payplaza.ecr_sdk_integration.BuildConfig
import com.cm.payplaza.ecr_sdk_integration.koin.repositoryModule
import com.cm.payplaza.ecr_sdk_integration.koin.testModule
import com.cm.payplaza.ecr_sdk_integration.koin.viewModelModule
import com.cm.payplaza.ecr_sdk_integration.utils.LocaleHelper
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class EcrApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // Init koin
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@EcrApplication)
            modules(viewModelModule, repositoryModule, testModule)
        }
        // Init Timber
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Init Postintegration
        AndroidPosIntegration.init(this@EcrApplication)

        // Set language
        val current = LocaleHelper.getLocale()
        val savedLang = LocaleHelper.getPersistedData(this@EcrApplication)
        if(current != savedLang) {
            LocaleHelper.setLocale(this@EcrApplication, savedLang)
        }
    }
}