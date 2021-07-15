package fr.francoisgaucher.poc_sonar_analysis

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.francoisgaucher.poc_sonar_analysis.extensions.getViewModelScope
import fr.francoisgaucher.poc_sonar_analysis.modals.CurrentWeather
import fr.francoisgaucher.poc_sonar_analysis.repositories.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivityViewModel(
    coroutineScopeProvider: CoroutineScope? = null,
    private val defaultDispatcher: CoroutineDispatcher,
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    private val coroutineScope = getViewModelScope(coroutineScopeProvider)
    val mutableCurrentWeatherLiveData = MutableLiveData<CurrentWeather>()

    fun getCurrentWeather(cityName: String) {
        coroutineScope.launch(defaultDispatcher) {
            val currentWeather = weatherRepository.getCurrentWeather(cityName)
            mutableCurrentWeatherLiveData.postValue(currentWeather)
        }
    }
}