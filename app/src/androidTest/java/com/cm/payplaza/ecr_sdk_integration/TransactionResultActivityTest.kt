package com.cm.payplaza.ecr_sdk_integration

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.entity.Transaction
import com.cm.payplaza.ecr_sdk_integration.entity.TransactionError
import com.cm.payplaza.ecr_sdk_integration.entity.TransactionResponse
import com.cm.payplaza.ecr_sdk_integration.fragment.receiptView.ReceiptFragment
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class TransactionResultActivityTest: KoinComponent {

    private val localDataRepository: LocalDataRepository by inject()
    private val purchaseTransaction: Transaction by inject(qualifier = named("purchaseTransaction"))
    private val transactionResultSuccess: TransactionResponse by inject(qualifier = named("transactionResultSuccess"))
    private val transactionError: TransactionError by inject(qualifier = named("transactionError"))

    @Before
    fun initData() {
        IdlingPolicies.setMasterPolicyTimeout(3, TimeUnit.MINUTES)
        IdlingPolicies.setIdlingResourceTimeout(3, TimeUnit.MINUTES)
        localDataRepository.setTransaction(purchaseTransaction)
        localDataRepository.setTransactionError(transactionError)
        localDataRepository.setTransactionResponse(transactionResultSuccess)
    }

    @After
    fun deInitData() {
        localDataRepository.clearTransactionData()
    }

    @Test
    fun showReceiptFragment() {
        val fragmentScenario = launchFragmentInContainer<ReceiptFragment>(
            initialState = Lifecycle.State.RESUMED, themeResId = R.style.AppTheme
        )
        
        onView(withId(R.id.textview_receipt)).check(matches(isDisplayed()))
        Thread.sleep(2000)
    }
}
