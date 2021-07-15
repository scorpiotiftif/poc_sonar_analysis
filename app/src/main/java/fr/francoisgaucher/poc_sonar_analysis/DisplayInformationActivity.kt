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

        if (intent == null || intent.extras == null || !intent.extras!!.containsKey(BUNDLE_CURRENT_WEATHER)) {
            return
        }

        intent?.extras?.getParcelable<CurrentWeather>(BUNDLE_CURRENT_WEATHER)?.let {
            currentWeather = it
        }

        val cityNameTextView = findViewById<TextView>(R.id.display_information_activity_city_name)
        cityNameTextView.text = currentWeather.location.name

        val temperatureTextView = findViewById<TextView>(R.id.display_information_activity_temperature)
        temperatureTextView.text = getString(R.string.temperatures, currentWeather.current.tempC, currentWeather.current.tempF)

        val actualTemperatureTextView = findViewById<TextView>(R.id.display_information_activity_actual_temperature)
        actualTemperatureTextView.text = currentWeather.current.feelslikeC.toString()

        val actualFeelingTemperatureTextView = findViewById<TextView>(R.id.display_information_activity_actual_feeling)
        actualFeelingTemperatureTextView.text = currentWeather.current.condition.text

        Glide.with(findViewById<ImageView>(R.id.display_information_activity_actual_feeling_img))
            .load("$PREFIX_HTTPS_URL${currentWeather.current.condition.icon}")
            .into(findViewById(R.id.display_information_activity_actual_feeling_img))


        val countryTextView = findViewById<TextView>(R.id.display_information_activity_country)
        countryTextView.text = currentWeather.location.country

        val latitudeTextView = findViewById<TextView>(R.id.display_information_activity_lat)
        latitudeTextView.text = currentWeather.location.lat.toString()

        val longitudeTextView = findViewById<TextView>(R.id.display_information_activity_long)
        longitudeTextView.text = currentWeather.location.lon.toString()

        val localtimeTextView = findViewById<TextView>(R.id.display_information_activity_localtime)
        localtimeTextView.text = currentWeather.location.localtime

        val localtimeEpochTextView = findViewById<TextView>(R.id.display_information_activity_localtime_epoch)
        localtimeEpochTextView.text = currentWeather.location.localtimeEpoch.toString()

        val regionTextView = findViewById<TextView>(R.id.display_information_activity_region)
        regionTextView.text = currentWeather.location.region
    }

    companion object {
        @VisibleForTesting
        const val BUNDLE_CURRENT_WEATHER = "BUNDLE_CURRENT_WEATHER"
        private const val PREFIX_HTTPS_URL = "https"

        fun newIntent(context: Context, currentWeather: CurrentWeather): Intent {
            return Intent(context, DisplayInformationActivity::class.java).apply {
                putExtras(bundleOf(Pair(BUNDLE_CURRENT_WEATHER, currentWeather)))
            }
        }
    }
}