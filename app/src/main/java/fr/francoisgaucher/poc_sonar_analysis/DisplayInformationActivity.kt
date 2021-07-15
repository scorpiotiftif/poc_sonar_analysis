package fr.francoisgaucher.poc_sonar_analysis

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import fr.francoisgaucher.poc_sonar_analysis.modals.CurrentWeather

class DisplayInformationActivity : AppCompatActivity() {

    private lateinit var currentWeather: CurrentWeather

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_information_activity)

        intent.extras!!.getParcelable<CurrentWeather>(BUNDLE_CURRENT_WEATHER)!!.let {
            currentWeather = it
        }

        currentWeather = currentWeather

        findViewById<TextView>(R.id.display_information_activity_city_name).text = currentWeather.location.name
        findViewById<TextView>(R.id.display_information_activity_temperature).text =
            "" + currentWeather.current.temp_c + "°C / " + currentWeather.current.temp_f + "°F"
        findViewById<TextView>(R.id.display_information_activity_actual_temperature).text = "" + currentWeather.current.feelslike_c
        findViewById<TextView>(R.id.display_information_activity_actual_feeling).text = "" + currentWeather.current.condition.text
        Glide.with(findViewById<ImageView>(R.id.display_information_activity_actual_feeling_img))
            .load("https:"+currentWeather.current.condition.icon)
            .into(findViewById(R.id.display_information_activity_actual_feeling_img))
    }

    companion object {
        @VisibleForTesting
        const val BUNDLE_CURRENT_WEATHER = "BUNDLE_CURRENT_WEATHER"
        fun newIntent(context: Context, currentWeather: CurrentWeather): Intent {
            return Intent(context, DisplayInformationActivity::class.java).apply {
                putExtras(bundleOf(Pair(BUNDLE_CURRENT_WEATHER, currentWeather)))
            }
        }
    }
}