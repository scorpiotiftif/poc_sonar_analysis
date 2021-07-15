package fr.francoisgaucher.poc_sonar_analysis

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import com.google.android.material.internal.ContextUtils.getActivity
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Before
    fun setup(){
        Intents.init()
    }

    @After
    fun finish(){
        Intents.release()
    }

    private fun grantPermission() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        if (Build.VERSION.SDK_INT >= 23) {
            val allowPermission = UiDevice.getInstance(instrumentation).findObject(
                UiSelector().text(
                    when {
                        Build.VERSION.SDK_INT == 23 -> "Allow"
                        Build.VERSION.SDK_INT <= 28 -> "ALLOW"
                        Build.VERSION.SDK_INT == 29 -> "Allow only while using the app"
                        else -> "While using the app"
                    }
                )
            )
            if (allowPermission.exists()) {
                allowPermission.click()
            }
        }
    }

    @Test
    fun checkLocationServiceEnabled() {
        val intent = Intent(ApplicationProvider.getApplicationContext<Context>(), MainActivity::class.java)
        ActivityScenario.launch<MainActivity>(intent)

        val device = UiDevice.getInstance(getInstrumentation())

        onView(withId(R.id.main_activity_my_location))
            .perform(click())

        grantPermission()

        val useLocation: UiObject = device.findObject(
            UiSelector().text("Use location")
        )
        if(useLocation.exists() && !useLocation.isSelected){
            useLocation.click()
            device.pressBack()
            Thread.sleep(1000)
            onView(withId(R.id.main_activity_my_location))
                .perform(click())
            Thread.sleep(4000)

        }
        Thread.sleep(1000)

        onView(withId(R.id.main_activity_current_weather_button)).perform(click())
        onView(withId(R.id.main_activity_current_weather_button)).perform(click())

        Thread.sleep(2000)

        onView(withId(R.id.display_information_activity_city_name)).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun typeCityNameAndCheckIfDataAreReceived() {
        val intent = Intent(ApplicationProvider.getApplicationContext<Context>(), MainActivity::class.java)
        ActivityScenario.launch<MainActivity>(intent)

        Thread.sleep(100)

        onView(withId(R.id.main_activity_input))
            .perform(typeText("PARIS"), closeSoftKeyboard())
        Thread.sleep(100)

        onView(withId(R.id.main_activity_current_weather_button)).perform(click())

        Thread.sleep(4000)

        onView(withId(R.id.display_information_activity_city_name)).check(ViewAssertions.matches(withText("PARIS")))
    }

    @Test
    fun detectOwnGeolocationAndCheckIfDataAreReceived() {

        val intent = Intent(ApplicationProvider.getApplicationContext<Context>(), MainActivity::class.java)
        ActivityScenario.launch<MainActivity>(intent)

        onView(withId(R.id.main_activity_my_location))
            .perform(click())
        grantPermission()
        Thread.sleep(3000)

        onView(withId(R.id.main_activity_current_weather_button)).perform(click())

        Thread.sleep(3000)

        onView(withId(R.id.display_information_activity_city_name)).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun doubleDetectOwnGeolocationAndCheckIfDataAreReceived() {

        val intent = Intent(ApplicationProvider.getApplicationContext<Context>(), MainActivity::class.java)
        ActivityScenario.launch<MainActivity>(intent)

        onView(withId(R.id.main_activity_my_location))
            .perform(click())
        grantPermission()
        Thread.sleep(3000)
        onView(withId(R.id.main_activity_my_location))
            .perform(click())
        Thread.sleep(3000)

        onView(withId(R.id.main_activity_current_weather_button)).perform(click())

        Thread.sleep(3000)

        onView(withId(R.id.display_information_activity_city_name)).check(ViewAssertions.matches(isDisplayed()))
    }



    @Test
    fun goToNextScreen() {

        val intent = Intent(ApplicationProvider.getApplicationContext<Context>(), MainActivity::class.java)
        ActivityScenario.launch<MainActivity>(intent)

        onView(withId(R.id.main_activity_current_weather_button)).perform(click())
        grantPermission()

        Thread.sleep(4000)
        onView(withId(R.id.main_activity_current_weather_button)).perform(click())
        Thread.sleep(4000)
        onView(withId(R.id.display_information_activity_city_name)).check(ViewAssertions.matches(isDisplayed()))
    }
}