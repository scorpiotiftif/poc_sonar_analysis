package fr.francoisgaucher.poc_sonar_analysis

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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

        mainActivityViewModel.mutableCurrentWeatherLiveData.observe(this) {
            startActivity(
                DisplayInformationActivity.newIntent(this, it),
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
            )
        }

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
                    mainActivityViewModel.getCurrentWeather(cityLocation!!)
                    cityLocation = null
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

    private fun checkPermissions(): Boolean {
        return (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
            )
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient?.lastLocation?.addOnCompleteListener(OnCompleteListener<Location?> { task ->
                    val location = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        defineCityLocationWithLocation(location)
                    }
                })
            } else {
                startActivity(Intent(ACTION_LOCATION_SOURCE_SETTINGS))
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 5
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper() ?: Looper.getMainLooper())
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            defineCityLocationWithLocation(locationResult.lastLocation)
        }
    }

    private fun defineCityLocationWithLocation(location: Location) {
        val gcd = Geocoder(this@MainActivity.applicationContext, Locale.getDefault())
        val addresses: List<Address> = gcd.getFromLocation(location.latitude, location.longitude, 1)
        if (addresses.isNotEmpty()) {
            cityLocation = addresses[0].locality
            findViewById<EditText>(R.id.main_activity_input).setText(cityLocation)
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastLocation()
        }
    }

    companion object {
        private const val PERMISSION_ID = 44
    }
}