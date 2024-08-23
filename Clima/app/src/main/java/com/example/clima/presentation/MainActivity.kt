package com.example.clima.presentation

import WeatherData
import WeatherViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var latitude by remember { mutableStateOf("") }
            var longitude by remember { mutableStateOf("") }
            var showDialog by remember { mutableStateOf(true) }

            if (showDialog) {
                InputCoordinatesDialog(
                    latitude = latitude,
                    onLatitudeChange = { latitude = it },
                    longitude = longitude,
                    onLongitudeChange = { longitude = it },
                    onConfirm = {
                        weatherViewModel.fetchWeather(
                            lat = latitude.toDouble(),
                            lon = longitude.toDouble()
                        )
                        showDialog = false
                    }
                )
            }

            val weatherData by weatherViewModel.weatherData.observeAsState()
            WeatherScreen(weatherData = weatherData)
        }
    }
}

@Composable
fun InputCoordinatesDialog(
    latitude: String,
    onLatitudeChange: (String) -> Unit,
    longitude: String,
    onLongitudeChange: (String) -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /* No dismiss */ },
        text = {
            Column {
                OutlinedTextField(
                    value = latitude,
                    onValueChange = onLatitudeChange,
                    label = { Text("Latitude") },
                    modifier = Modifier
                        .size(width = 70.dp, height = 15.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = longitude,
                    onValueChange = onLongitudeChange,
                    label = { Text("Longitude") },
                    modifier = Modifier
                        .size(width = 70.dp, height = 15.dp)
                )


            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("OK")
            }
        }
    )
}

@Composable
fun WeatherScreen(weatherData: WeatherData?) {
    MaterialTheme {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            weatherData?.let {
                Text(text = "Temperature: ${it.currentTemp}°C")
                Text(text = "Humidity: ${it.currentHumidity}%")
                Text(text = "Max Temp: ${it.maxTemp?.toString() ?: "N/A"}°C")
                Text(text = "Min Temp: ${it.minTemp?.toString() ?: "N/A"}°C")
            } ?: Text(text = "Loading...")
        }
    }
}

@Preview
@Composable
fun PreviewWeatherScreen() {
    WeatherScreen(
        weatherData = WeatherData(
            currentTemp = 25.0f,
            currentHumidity = 60,
            maxTemp = 30.0f,
            minTemp = 20.0f
        )
    )
}
