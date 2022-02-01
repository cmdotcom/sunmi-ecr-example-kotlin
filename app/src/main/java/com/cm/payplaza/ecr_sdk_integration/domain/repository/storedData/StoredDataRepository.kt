package com.cm.payplaza.ecr_sdk_integration.domain.repository.storedData

interface StoredDataRepository {
    fun saveOrderReference(newOrderReference: Int)
    fun loadOrderReference(): Int
}