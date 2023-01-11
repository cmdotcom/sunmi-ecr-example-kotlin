package com.cm.payplaza.ecr_sdk_integration.utils

import android.content.Context
import android.preference.PreferenceManager
import java.util.*

object LocaleHelper {

    private const val SHARED_PREFERENCES = "locale_preferences"
    private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
    const val DEFAULT_COUNTRY_CODE = "NL"
    const val DEFAULT_LANGUAGE = "en"

    fun getLocale(context: Context): Locale {
        return Locale(getLanguage(context))
    }

    fun getLanguage(context: Context): String? {
        return getPersistedData(context, Locale.getDefault().language)
    }

    private fun getPersistedData(context: Context, defaultLanguage: String): String? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage)
    }

    fun setLocale(context: Context, language: String, country: String) {
        persist(context, language)
        updateResources(context, language, country)
    }

    fun getLocale(): String {
        return Locale.getDefault().language
    }

    fun getLocation(): String {
        return Locale.getDefault().country.uppercase()
    }

    fun getPersistedData(context: Context): String {
        val preferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        return preferences.getString(SELECTED_LANGUAGE, DEFAULT_LANGUAGE)
            ?: run { DEFAULT_LANGUAGE }
    }

    private fun persist(context: Context, language: String) {
        val preferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(SELECTED_LANGUAGE, language)
        editor.apply()
    }

    private fun updateResources(context: Context, language: String, country: String) {
        val locale = Locale(language, country.uppercase())
        Locale.setDefault(locale)
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        context.createConfigurationContext(configuration)
    }
}
