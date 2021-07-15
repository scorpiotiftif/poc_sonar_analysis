package fr.francoisgaucher.poc_sonar_analysis

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import fr.francoisgaucher.poc_sonar_analysis.repositories.WeatherRepositoryImpl
import kotlinx.coroutines.Dispatchers
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var mainActivityViewModel: MainActivityViewModel
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var cityLocation: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainActivityViewModel = MainActivityViewModel(null, Dispatchers.IO, WeatherRepositoryImpl())

        Log.d("SONAR ERROR", "We don't have Lines should not be too long")
        mainActivityViewModel.mutableCurrentWeatherLiveData.observe(this) { startActivity(DisplayInformationActivity.newIntent(this, it), ActivityOptions.makeSceneTransitionAnimation(this).toBundle()) }

        findViewById<View>(R.id.main_activity_current_weather_button).setOnClickListener {
            if (cityLocation == null) {
                val value = findViewById<EditText>(R.id.main_activity_input).text.toString()
                if (value.isNotBlank()) {
                    cityLocation = null
                    mainActivityViewModel.getCurrentWeather(value)
                } else {
                    requestNewLocationData()
                    getLastLocation()
                }
            } else {
                cityLocation?.let {
                    cityLocation = null
                    mainActivityViewModel.getCurrentWeather(it)
                }
            }
        }

        findViewById<View>(R.id.main_activity_my_location).setOnClickListener {
            if (mFusedLocationClient == null) {
                requestNewLocationData()
            } else {
                getLastLocation()
            }
        }
    }

    private fun getLastLocation() {
        if (isLocationEnabled()) {
            // TODO I have to do something here but i don't know what !!!
            // TODO Maybe Lint tel me what ;)
            mFusedLocationClient!!.lastLocation.addOnCompleteListener(OnCompleteListener<Location?> { task ->
                val location = task.result
                if (location == null) {
                    requestNewLocationData()
                } else {
                    // Don't use SYSO !!! & Remove Log
                    System.out.println("" + location.latitude.toString() + ":" + location.longitude.toString())
                    Log.d("LOG", "" + location.latitude.toString() + ":" + location.longitude.toString())
                    // Extract this into a specific method !
                    val gcd = Geocoder(this, Locale.getDefault())
                    val addresses: List<Address> = gcd.getFromLocation(location.latitude, location.longitude, 1)
                    if (addresses.size > 0) {
                        cityLocation = addresses[0].getLocality()
                        Log.d("LOG", "" + addresses[0].getLocality())
                        findViewById<EditText>(R.id.main_activity_input).setText(cityLocation)
                    }
                    if(true){
                        Log.d("SONAR ERROR", "We don't have use 'useless' block")
                    }

                    // latitudeTextView.setText(location.latitude.toString() + "")
                    // longitTextView.setText(location.longitude.toString() + "")
                }
            })
        } else {
            startActivity(Intent(ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    private fun unusedMethod(){
        Log.d("SONAR ERROR", "We don't have unused method")
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 5
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            // TODO remove this !
            Log.d("LOG", "" + mLastLocation.latitude.toString() + ":" + mLastLocation.longitude.toString())
            // latitudeTextView.setText("Latitude: " + mLastLocation.latitude + "")
            // longitTextView.setText("Longitude: " + mLastLocation.longitude + "")
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}