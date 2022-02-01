package com.cm.payplaza.ecr_sdk_integration.utils

import android.content.Context
import java.util.*

class LocaleHelper {
    companion object {
        private const val SHARED_PREFERENCES = "locale_preferences"
        private const val  SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
        private const val DEFAULT_LOCALE = "en"

        fun setLocale(context: Context, language: String) {
            persist(context, language)
            updateResources(context, language)
        }

        fun getLocale(): String {
            return Locale.getDefault().language
        }

        fun getPersistedData(context: Context): String {
            val preferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
            return preferences.getString(SELECTED_LANGUAGE, DEFAULT_LOCALE) ?: run { DEFAULT_LOCALE }
        }

        private fun persist(context: Context, language: String) {
            val preferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString(SELECTED_LANGUAGE, language)
            editor.apply()
        }

        private fun updateResources(context: Context, language: String) {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val configuration = context.resources.configuration
            configuration.setLocale(locale)
            configuration.setLayoutDirection(locale)
            context.createConfigurationContext(configuration)
        }
    }
}