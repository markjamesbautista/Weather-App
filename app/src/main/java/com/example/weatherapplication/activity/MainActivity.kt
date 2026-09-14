package com.example.weatherapplication.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapplication.ui.screens.LoginScreen
import com.example.weatherapplication.ui.screens.SignUpScreen
import com.example.weatherapplication.ui.screens.WeatherMainScreen
import com.example.weatherapplication.ui.theme.WeatherApplicationTheme
import com.example.weatherapplication.viewmodel.AuthViewModel
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
    val authViewModel: AuthViewModel = hiltViewModel()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    NavHost(
        navController = navController, 
        startDestination = if (isLoggedIn) "main" else "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginClick = { email, password ->
                    // For demo, any non-empty input works
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        authViewModel.login(email)
                        navController.navigate("main") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                },
                onSignUpClick = {
                    navController.navigate("signup")
                }
            )
        }
        composable("signup") {
            SignUpScreen(
                onConfirmClick = { name, email, password ->
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        authViewModel.signup(name, email)
                        navController.navigate("main") {
                            popUpTo("login") { inclusive = true }
                        }
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
