package fr.francoisgaucher.poc_sonar_analysis.repositories

import fr.francoisgaucher.poc_sonar_analysis.modals.CurrentWeather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getCurrentWeather(cityName: String): CurrentWeather
}