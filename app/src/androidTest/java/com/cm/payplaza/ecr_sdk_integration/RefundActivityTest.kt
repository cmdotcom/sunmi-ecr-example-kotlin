package com.cm.payplaza.ecr_sdk_integration

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cm.payplaza.ecr_sdk_integration.activity.refund.RefundActivity
import com.cm.payplaza.ecr_sdk_integration.domain.repository.localData.LocalDataRepository
import com.cm.payplaza.ecr_sdk_integration.entity.Transaction
import com.cm.payplaza.ecr_sdk_integration.entity.TransactionError
import com.cm.payplaza.ecr_sdk_integration.entity.TransactionResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class RefundActivityTest: KoinComponent {
    private val localDataRepository: LocalDataRepository by inject()
    private val purchaseTransaction: Transaction by inject(qualifier = named("purchaseTransaction"))
    private val transactionResultSuccess: TransactionResponse by inject(qualifier = named("transactionResultSuccess"))
    private val transactionError: TransactionError by inject(qualifier = named("transactionError"))

    @get:Rule
    val activityRule = ActivityScenarioRule(RefundActivity::class.java)
    private lateinit var signInIdlingResource: IdlingResource

    @Before
    fun setUpTest() {
        IdlingPolicies.setMasterPolicyTimeout(3, TimeUnit.MINUTES)
        IdlingPolicies.setIdlingResourceTimeout(3, TimeUnit.MINUTES)
        localDataRepository.setTransaction(purchaseTransaction)
        localDataRepository.setTransactionError(transactionError)
        localDataRepository.setTransactionResponse(transactionResultSuccess)
        activityRule.scenario.onActivity {
            signInIdlingResource = it.mIdlingRes
        }
        IdlingRegistry.getInstance().register(signInIdlingResource)
    }

    @After
    fun cleanup() {
        IdlingRegistry.getInstance().unregister(signInIdlingResource)
    }

    @Test
    fun refundDataInsertTest() {
        onView(ViewMatchers.withId(R.id.component_numeric_keypad)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Thread.sleep(200)
        onView(ViewMatchers.withId(R.id.keypad_button_1)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.keypad_button_7)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.keypad_button_0)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.keypad_button_9)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.keypad_button_3)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.amount_view_amount)).check(ViewAssertions.matches(ViewMatchers.withText("*****")))
        Thread.sleep(200)
        onView(ViewMatchers.withId(R.id.keypad_button_confirm)).perform(ViewActions.click())
        Thread.sleep(200)
        onView(ViewMatchers.withId(R.id.keypad_button_1)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.keypad_button_0)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.keypad_button_0)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.amount_view_amount)).check(ViewAssertions.matches(ViewMatchers.withText("1.00")))
        Thread.sleep(200)
        onView(ViewMatchers.withId(R.id.keypad_button_confirm)).perform(ViewActions.click())
        Thread.sleep(200)
        if("NL" != localDataRepository.getTerminalData()?.storeCountry) {
            onView(ViewMatchers.withId(R.id.keypad_button_1)).perform(ViewActions.click())
            onView(ViewMatchers.withId(R.id.keypad_button_2)).perform(ViewActions.click())
            onView(ViewMatchers.withId(R.id.keypad_button_3)).perform(ViewActions.click())
            onView(ViewMatchers.withId(R.id.keypad_button_4)).perform(ViewActions.click())
            onView(ViewMatchers.withId(R.id.keypad_button_5)).perform(ViewActions.click())
            onView(ViewMatchers.withId(R.id.keypad_button_6)).perform(ViewActions.click())
            onView(ViewMatchers.withId(R.id.amount_view_amount)).check(ViewAssertions.matches(ViewMatchers.withText("123456")))
            Thread.sleep(200)
            onView(ViewMatchers.withId(R.id.keypad_button_confirm)).perform(ViewActions.click())
            Thread.sleep(200)
            onView(ViewMatchers.withId(R.id.date_component_view)).perform(ViewActions.click())
        }
    }
}