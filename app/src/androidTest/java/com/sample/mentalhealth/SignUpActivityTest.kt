package com.sample.mentalhealth

import android.util.Log
import android.view.WindowManager
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Root
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sample.mentalhealth.login_registration.SignInActivity
import com.sample.mentalhealth.login_registration.SignUpActivity
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SignUpActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(SignUpActivity::class.java)

    // 1. Check toast when fields are empty
    @Test
    fun emptyFields_showsToast() {
        onView(withId(R.id.btnSignUp)).perform(click())

        onView(ViewMatchers.withText("Enter all details"))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }

    // 2. Fill all fields → successful registration
    @Test
    fun validInput_registrationSuccess() {
        onView(withId(R.id.atvUsernameReg)).perform(typeText("John"))
        closeSoftKeyboard()
        onView(withId(R.id.atvEmailReg)).perform(typeText("john@example.com"))
        closeSoftKeyboard()
        onView(withId(R.id.atvPasswordReg)).perform(typeText("123"))
        closeSoftKeyboard()

        Intents.init()

        onView(withId(R.id.btnSignUp)).perform(click())

        // Check if success toast is shown
        onView(withText("Registration successful"))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))

        // Verify SignInActivity is launched
        intended(hasComponent(SignInActivity::class.java.name))

        Intents.release()
    }

    // 3. Click "Sign In" → Navigate to SignInActivity
    @Test
    fun clickSignIn_opensSignInActivity() {
        Intents.init()

        onView(withId(R.id.tvSignIn)).perform(click())

        intended(hasComponent(SignInActivity::class.java.name))

        Intents.release()
    }


    class ToastMatcher : TypeSafeMatcher<Root>() {
        override fun describeTo(description: Description) {
            description.appendText("is toast")
        }

        public override fun matchesSafely(root: Root): Boolean {
            val type = root.windowLayoutParams.get().type
            Log.d("ToastMatcher", "Checking root. Type: $type, DecorView: ${root.decorView}")
            if (type == WindowManager.LayoutParams.TYPE_TOAST) {
                val windowToken = root.decorView.windowToken
                val appToken = root.decorView.applicationWindowToken
                Log.d("ToastMatcher", "Is TYPE_TOAST. WindowToken: $windowToken, AppToken: $appToken")
                return windowToken === appToken
            }
            return false
        }
    }
}
