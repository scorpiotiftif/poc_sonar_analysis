package fr.francoisgaucher.poc_sonar_analysis.api

import fr.francoisgaucher.poc_sonar_analysis.modals.CurrentWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface WeatherAPI {
    @GET("current.json")
    suspend fun getCurrentWeather(@QueryMap queries: Map<String, String>) : CurrentWeather
}