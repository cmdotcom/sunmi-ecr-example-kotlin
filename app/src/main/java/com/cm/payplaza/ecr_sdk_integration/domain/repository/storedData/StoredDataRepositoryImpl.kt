package com.cm.payplaza.ecr_sdk_integration.domain.repository.storedData

import android.content.SharedPreferences

class StoredDataRepositoryImpl(private val sharedPreferences: SharedPreferences):
    StoredDataRepository {
    companion object {
        private const val ORDER_REFERENCE_KEY = "order_reference_key"
    }
    override fun saveOrderReference(newOrderReference: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(ORDER_REFERENCE_KEY, newOrderReference).apply()
    }

    override fun loadOrderReference(): Int {
        return sharedPreferences.getInt(ORDER_REFERENCE_KEY, 0)
    }
}