package fr.francoisgaucher.poc_sonar_analysis

import android.content.Context
import android.content.Intent
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
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import com.google.android.material.internal.ContextUtils.getActivity
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
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

    @Test
    fun checkLocationServiceEnabled() {
        val intent = Intent(ApplicationProvider.getApplicationContext<Context>(), MainActivity::class.java)
        val scenario = ActivityScenario.launch<MainActivity>(intent)

        val device = UiDevice.getInstance(getInstrumentation())
//        device.openQuickSettings()
//        val cancelButton: UiObject = device.findObject(
//            UiSelector().text("Location")
//        )

//        if(cancelButton.exists() && (cancelButton.isSelected || cancelButton.isChecked || cancelButton.isEnabled)){
//            cancelButton.click()
//            Thread.sleep(100)
//
//            device.pressBack()
//            Thread.sleep(100)
//            device.pressBack()
//        }else{
//            device.pressBack()
//            Thread.sleep(100)
//
//            device.pressBack()
//        }

//        Thread.sleep(4000)

        onView(withId(R.id.main_activity_my_location))
            .perform(click())
//

        val onlyThisTime: UiObject = device.findObject(
            UiSelector().text("Only this time")
        )
        if(onlyThisTime.exists()){
            onlyThisTime.click()
        }

        val allow: UiObject = device.findObject(
            UiSelector().text("ALLOW")
        )
        if(allow.exists()){
            allow.click()
        }

        val agree: UiObject = device.findObject(
            UiSelector().text("AGREE")
        )
        if(agree.exists()){
            agree.click()
        }

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

        val text = getText(onView(withId(R.id.main_activity_input))).toUpperCase()

        onView(withId(R.id.main_activity_current_weather_button)).perform(click())
        onView(withId(R.id.main_activity_current_weather_button)).perform(click())

        Thread.sleep(2000)

        onView(withId(R.id.display_information_activity_city_name)).check(ViewAssertions.matches(isDisplayed()))
//        onView(withId(R.id.display_information_activity_city_name)).check(ViewAssertions.matches(withText(text)))
    }

    @Test
    fun typeCityNameAndCheckIfDataAreReceived() {
        val intent = Intent(ApplicationProvider.getApplicationContext<Context>(), MainActivity::class.java)
        val scenario = ActivityScenario.launch<MainActivity>(intent)

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

        val text = getText(onView(withId(R.id.main_activity_input)))
        onView(withId(R.id.main_activity_current_weather_button)).perform(click())
        onView(withId(R.id.main_activity_current_weather_button)).perform(click())

        Thread.sleep(6000)

        onView(withId(R.id.display_information_activity_city_name)).check(ViewAssertions.matches(isDisplayed()))
    }



    @Test
    fun goToNextScreen() {

        val intent = Intent(ApplicationProvider.getApplicationContext<Context>(), MainActivity::class.java)
        val scenario = ActivityScenario.launch<MainActivity>(intent)

        onView(withId(R.id.main_activity_current_weather_button)).perform(click())

        Thread.sleep(4000)
        onView(withId(R.id.main_activity_current_weather_button)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.display_information_activity_city_name)).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun requestPermission() {

        val intent = Intent(ApplicationProvider.getApplicationContext<Context>(), MainActivity::class.java)
        val scenario = ActivityScenario.launch<MainActivity>(intent)

        onView(withId(R.id.main_activity_current_weather_button)).perform(click())

        Thread.sleep(4000)
        onView(withId(R.id.main_activity_current_weather_button)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.display_information_activity_city_name)).check(ViewAssertions.matches(isDisplayed()))
    }

    private fun getText(matcher: ViewInteraction): String {
        var text = String()
        matcher.perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(TextView::class.java)
            }

            override fun getDescription(): String {
                return "Text of the view"
            }

            override fun perform(uiController: UiController, view: View) {
                val tv = view as TextView
                text = tv.text.toString()
            }
        })

        return text
    }
}