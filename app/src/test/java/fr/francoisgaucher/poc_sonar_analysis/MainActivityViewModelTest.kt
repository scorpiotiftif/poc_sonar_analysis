package fr.francoisgaucher.poc_sonar_analysis

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import fr.francoisgaucher.poc_sonar_analysis.extensions.getOrAwaitValue
import fr.francoisgaucher.poc_sonar_analysis.modals.Condition
import fr.francoisgaucher.poc_sonar_analysis.modals.Current
import fr.francoisgaucher.poc_sonar_analysis.modals.CurrentWeather
import fr.francoisgaucher.poc_sonar_analysis.modals.Location
import fr.francoisgaucher.poc_sonar_analysis.repositories.WeatherRepository
import fr.francoisgaucher.poc_sonar_analysis.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockkClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainActivityViewModelTest {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var repo: WeatherRepository

    // Run tasks synchronously
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    // Sets the main coroutines dispatcher to a TestCoroutineScope for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun initViewModel() {
        // Initialize the ViewModel after the [MainCoroutineRule] is applied so that it has the
        // right test dispatcher.
        Dispatchers.setMain(testDispatcher)

        repo = mockkClass(WeatherRepository::class)
        viewModel = MainActivityViewModel(testScope, testDispatcher,repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getMutableCurrentWeatherLiveData() {
    }

    @Test
    fun getCurrentWeather() {

        testScope.runBlockingTest {
            //mockk.`when`(repo.getCurrentWeather("")).thenReturn(CURRET_WEATHER)
            coEvery { repo.getCurrentWeather("") }.coAnswers { CURRET_WEATHER }
            // This block WORKS
            var currentWeather = repo.getCurrentWeather("")
            Assert.assertEquals(CURRET_WEATHER, currentWeather)
            Assert.assertEquals(CURRET_WEATHER.toString(), currentWeather.toString())

            // This block DOES NOT WORKS
            viewModel.getCurrentWeather("")
            currentWeather = viewModel.mutableCurrentWeatherLiveData.getOrAwaitValue()

            Assert.assertEquals(CURRET_WEATHER, currentWeather)
        }
    }

    companion object {
        private val CURRET_WEATHER = CurrentWeather(
            Current(
                Condition("", ""), 0.0, 0.0, 0.0
            ),
            Location("", 0.0, "", 0, 0.0, "", "")
        )
    }
}