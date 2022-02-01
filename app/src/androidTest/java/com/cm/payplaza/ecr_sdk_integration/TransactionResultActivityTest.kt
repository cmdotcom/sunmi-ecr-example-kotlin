package com.cm.payplaza.ecr_sdk_integration

import android.view.WindowManager
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.entity.Transaction
import com.cm.payplaza.ecr_sdk_integration.entity.TransactionError
import com.cm.payplaza.ecr_sdk_integration.entity.TransactionResponse
import com.cm.payplaza.ecr_sdk_integration.fragment.receiptView.ReceiptViewFragment
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

@RunWith(AndroidJUnit4::class)
class TransactionResultActivityTest: KoinComponent {
    private lateinit var fragmentScenario: FragmentScenario<ReceiptViewFragment>
    private val localDataRepository: LocalDataRepository by inject()
    private val purchaseTransaction: Transaction by inject(qualifier = named("purchaseTransaction"))
    private val transactionResultSuccess: TransactionResponse by inject(qualifier = named("transactionResultSuccess"))
    private val transactionError: TransactionError by inject(qualifier = named("transactionError"))

    @Before
    fun initData() {
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
        fragmentScenario = launchFragmentInContainer()
        fragmentScenario.onFragment {
            it.activity?.let { a ->
                val window = a.window
                window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
                window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
                window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
            }
        }
        onView(withId(R.id.receipt_view_close_button)).check(matches(isDisplayed()))
        onView(withId(R.id.receipt_view_close_button)).check(matches(isClickable()))
        onView(withId(R.id.receipt_view_button_print)).check(matches(isClickable()))
        onView(withId(R.id.textview_receipt)).check(matches(isDisplayed()))
        Thread.sleep(2000)
        onView(withId(R.id.receipt_view_close_button)).perform(ViewActions.click())
        onView(withId(R.id.textview_receipt)).check(matches(isDisplayed()))
        Thread.sleep(2000)
    }
}
