package fr.francoisgaucher.poc_sonar_analysis

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import fr.francoisgaucher.poc_sonar_analysis.modals.Condition
import fr.francoisgaucher.poc_sonar_analysis.modals.Current
import fr.francoisgaucher.poc_sonar_analysis.modals.CurrentWeather
import fr.francoisgaucher.poc_sonar_analysis.modals.Location
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
class DisplayActivityTest {

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun finish() {
        Intents.release()
    }

    @Test
    fun displaySomeInformations() {
        val intent = Intent(ApplicationProvider.getApplicationContext<Context>(), DisplayInformationActivity::class.java)
        intent.putExtras(Bundle().apply { putParcelable(DisplayInformationActivity.BUNDLE_CURRENT_WEATHER, CURRET_WEATHER) })
        ActivityScenario.launch<DisplayInformationActivity>(intent)

        onView(withId(R.id.display_information_activity_city_name)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.display_information_activity_city_name)).check(ViewAssertions.matches(withText(CURRET_WEATHER.location.name)))
    }

    companion object {
        private val CURRET_WEATHER = CurrentWeather(
            Current(
                Condition("", ""), 0.0, 0.0, 0.0
            ),
            Location("", 0.0, "", 0, 0.0, "PARIS", "")
        )
    }
}