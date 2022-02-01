package com.cm.payplaza.ecr_sdk_integration

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cm.payplaza.ecr_sdk_integration.activity.payment.PaymentActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.component.KoinComponent
import android.view.WindowManager.LayoutParams

@RunWith(AndroidJUnit4::class)
class PaymentActivityTest: KoinComponent {
    private lateinit var scenario: ActivityScenario<PaymentActivity>

    @Before
    fun setUpData() {
        scenario = ActivityScenario.launch(PaymentActivity::class.java)
        scenario.onActivity {
            val window = it.window
            window.addFlags(LayoutParams.FLAG_DISMISS_KEYGUARD)
            window.addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED)
            window.addFlags(LayoutParams.FLAG_TURN_SCREEN_ON)
        }
    }

    @After
    fun cleanup() {
        scenario.close()
    }

    @Test
    fun introduceAmount() {
        Thread.sleep(5000)
        onView(withId(R.id.component_numeric_keypad)).check(matches(isDisplayed()))
        // Introduce amount
        onView(withId(R.id.keypad_button_1)).perform(click())
        onView(withId(R.id.keypad_button_2)).perform(click())
        onView(withId(R.id.keypad_button_3)).perform(click())
        onView(withId(R.id.keypad_button_4)).perform(click())
        onView(withId(R.id.keypad_button_5)).perform(click())
        onView(withId(R.id.amount_view_amount)).check(matches(withText("123.45")))
        Thread.sleep(200)
        // Clear and introduce new amount
        onView(withId(R.id.keypad_button_backspace)).perform(click())
        onView(withId(R.id.keypad_button_6)).perform(click())
        onView(withId(R.id.keypad_button_7)).perform(click())
        onView(withId(R.id.keypad_button_8)).perform(click())
        onView(withId(R.id.keypad_button_9)).perform(click())
        onView(withId(R.id.keypad_button_0)).perform(click())
        onView(withId(R.id.amount_view_amount)).check(matches(withText("678.90")))
        Thread.sleep(200)
        onView(withId(R.id.keypad_button_backspace)).perform(click())
    }

    @Test
    fun introduceLargeAmount() {
        Thread.sleep(2000)
        onView(withId(R.id.component_numeric_keypad)).check(matches(isDisplayed()))
        // Introduce large amount (amount '12345.67' should appear before numbers of keypad get disabled)
        onView(withId(R.id.keypad_button_backspace)).perform(click())
        onView(withId(R.id.keypad_button_1)).perform(click())
        onView(withId(R.id.keypad_button_2)).perform(click())
        onView(withId(R.id.keypad_button_3)).perform(click())
        onView(withId(R.id.keypad_button_4)).perform(click())
        onView(withId(R.id.keypad_button_5)).perform(click())
        onView(withId(R.id.keypad_button_6)).perform(click())
        onView(withId(R.id.keypad_button_7)).perform(click())
        onView(withId(R.id.keypad_button_8)).perform(click())
        onView(withId(R.id.keypad_button_9)).perform(click())
        onView(withId(R.id.keypad_button_0)).perform(click())
        onView(withId(R.id.amount_view_amount)).check(matches(withText("12345.67")))
        Thread.sleep(200)
        onView(withId(R.id.keypad_button_backspace)).perform(click())
    }

    @Test
    fun introduceZeroAmount() {
        Thread.sleep(2000)
        onView(withId(R.id.component_numeric_keypad)).check(matches(isDisplayed()))
        // Introduce 0's amount (nothing should happen)
        onView(withId(R.id.keypad_button_backspace)).perform(click())
        onView(withId(R.id.keypad_button_0)).perform(click())
        onView(withId(R.id.keypad_button_0)).perform(click())
        onView(withId(R.id.keypad_button_0)).perform(click())
        onView(withId(R.id.keypad_button_0)).perform(click())
        onView(withId(R.id.keypad_button_0)).perform(click())
        onView(withId(R.id.keypad_button_0)).perform(click())
        onView(withId(R.id.amount_view_amount)).check(matches(withText("0.00")))
        Thread.sleep(200)
        onView(withId(R.id.keypad_button_backspace)).perform(click())
    }

    @Test
    fun goToTransaction() {
        Thread.sleep(2000)
        onView(withId(R.id.component_numeric_keypad)).check(matches(isDisplayed()))
        onView(withId(R.id.keypad_button_backspace)).perform(click())
        onView(withId(R.id.keypad_button_1)).perform(click())
        onView(withId(R.id.keypad_button_0)).perform(click())
        onView(withId(R.id.keypad_button_0)).perform(click())
        onView(withId(R.id.amount_view_amount)).check(matches(withText("1.00")))
    }

    @Test
    fun navMenuTest() {
        Thread.sleep(3000)
        scenario.onActivity { a ->
            a.onSupportNavigateUp()
        }
        onView(withId(R.id.payment_amount_insert_drawer)).check(matches(isDisplayed()))
        onView(withId(R.id.payment_navigation)).check(matches(isDisplayed()))
    }
}