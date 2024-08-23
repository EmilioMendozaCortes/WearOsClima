import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val _weatherData = MutableLiveData<WeatherData>()
    val weatherData: LiveData<WeatherData> get() = _weatherData

    fun fetchWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getWeather(
                    lat = lat,
                    lon = lon,
                    apiKey = "dc306e3ef52d13ac8d1ac06c9ca90e84"
                )
                val currentHumidity = response.current.humidity
                val currentTemp = response.current.temperature
                val maxTemp = response.daily.firstOrNull()?.temp?.max
                val minTemp = response.daily.firstOrNull()?.temp?.min

                _weatherData.postValue(
                    WeatherData(
                        currentTemp = currentTemp,
                        currentHumidity = currentHumidity,
                        maxTemp = maxTemp,
                        minTemp = minTemp
                    )
                )
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching weather data: ${e.message}")
            }
        }
    }

}

data class WeatherData(
    val currentTemp: Float,
    val currentHumidity: Int,
    val maxTemp: Float?,
    val minTemp: Float?
)
