package com.example.weatherapplication.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapplication.ui.screens.LoginScreen
import com.example.weatherapplication.ui.screens.SignUpScreen
import com.example.weatherapplication.ui.screens.WeatherMainScreen
import com.example.weatherapplication.ui.theme.WeatherApplicationTheme
import com.example.weatherapplication.viewmodel.WeatherViewModel
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            WeatherApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherAppNavigation(
                        fetchLocation = { viewModel ->
                            // Trigger location fetch using the existing ViewModel logic
                            // In a real app, you might use a dedicated LocationManager/Repository
                            // but here we follow the existing pattern.
                            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                val lat = location?.latitude ?: 14.9968
                                val lon = location?.longitude ?: 121.1710
                                viewModel.getWeather(lat, lon)
                            }.addOnFailureListener {
                                viewModel.getWeather(14.9968, 121.1710)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherAppNavigation(
    fetchLocation: (WeatherViewModel) -> Unit
) {
    val navController = rememberNavController()
    val weatherViewModel: WeatherViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginClick = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onSignUpClick = {
                    navController.navigate("signup")
                }
            )
        }
        composable("signup") {
            SignUpScreen(
                onConfirmClick = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("main") {
            WeatherMainScreen(
                viewModel = weatherViewModel,
                onFetchLocation = { fetchLocation(weatherViewModel) }
            )
        }
    }
}