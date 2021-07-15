package fr.francoisgaucher.poc_sonar_analysis.repositories

import fr.francoisgaucher.poc_sonar_analysis.api.WeatherAPI
import fr.francoisgaucher.poc_sonar_analysis.modals.CurrentWeather
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRepositoryImpl : WeatherRepository {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val weatherRepository: WeatherAPI = retrofit.create(WeatherAPI::class.java)
    private var defaultQueries: MutableMap<String, String> = mutableMapOf()

    override suspend fun getCurrentWeather(cityName: String): CurrentWeather {
        val buildDefaultParameters = buildDefaultParameters()
        buildDefaultParameters["q"] = cityName

        return weatherRepository.getCurrentWeather(buildDefaultParameters)
    }

    private fun buildDefaultParameters(): MutableMap<String, String> {
        defaultQueries["key"] = PASSWORD
        return defaultQueries
    }

    companion object {
        private const val BASE_URL = "https://api.weatherapi.com/v1/"
        private const val PASSWORD = "8aebefbce0a94cceaeb160932210107"
    }
}