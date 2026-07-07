package com.example.weatherapplication.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherapplication.adapter.ViewpagerAdapter
import com.example.weatherapplication.api.Response
import com.example.weatherapplication.api.WeatherList
import com.example.weatherapplication.databinding.FragmentWeatherBinding
import com.example.weatherapplication.viewmodel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherMainFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: WeatherViewModel by viewModel()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var sharedPref: SharedPreferences
    private var pagerAdapter: ViewpagerAdapter? = null

    private var pendingSave = false

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            fetchWeather()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        sharedPref = requireActivity().getSharedPreferences("WEATHER_PREFS", Context.MODE_PRIVATE)

        setupRefreshLayout()
        checkPermissionsAndGetLocation()
        observeUiState()
    }

    private fun setupRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            fetchWeather()
        }
    }

    private fun checkPermissionsAndGetLocation() {
        if (hasLocationPermission()) {
            fetchWeather()
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun fetchWeather() {
        pendingSave = true
        getLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            val lat = location?.latitude ?: 14.9968
            val lon = location?.longitude ?: 121.1710
            viewModel.getWeather(lat, lon)
        }.addOnFailureListener {
            // Even if location fails, try to get weather with default coords
            viewModel.getWeather(14.9968, 121.1710)
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.swipeRefresh.isRefreshing = state.isLoading
                    
                    if (!state.isLoading && state.weatherList.isNotEmpty()) {
                        updateWeatherUI(state.weatherList, pendingSave)
                        pendingSave = false
                    }
                    
                    binding.tvError.apply {
                        text = state.errorMessage
                        visibility = if (state.errorMessage != null) View.VISIBLE else View.GONE
                    }
                }
            }
        }
    }

    private fun updateWeatherUI(response: List<Response>, shouldSave: Boolean) {
        val currentJson = Gson().toJson(WeatherList(response))
        var savedList = getSavedWeatherList().list
        
        if (shouldSave) {
            // Create a history entry with a high-resolution timestamp
            val historyEntry = response.map { 
                it.copy(dt = (System.currentTimeMillis() / 1000).toInt()) 
            }
            
            // Prepend new items and take latest 50
            val newList = (historyEntry + savedList).take(50)
            saveWeatherList(WeatherList(newList))
            savedList = newList
        }
            
        val historyJson = Gson().toJson(WeatherList(savedList))

        if (pagerAdapter == null) {
            pagerAdapter = ViewpagerAdapter(this)
            binding.viewPager.apply {
                adapter = pagerAdapter
                offscreenPageLimit = 2
                (getChildAt(0) as? View)?.overScrollMode = View.OVER_SCROLL_NEVER
                
                // Disable SwipeRefreshLayout when not on the first tab
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        binding.swipeRefresh.isEnabled = (position == 0)
                    }
                })
            }

            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "Current Weather"
                    1 -> "History"
                    else -> ""
                }
            }.attach()
        }

        pagerAdapter?.updateData(currentJson, historyJson)
    }

    private fun saveWeatherList(weatherList: WeatherList) {
        sharedPref.edit().putString("saved_weather", Gson().toJson(weatherList)).apply()
    }

    private fun getSavedWeatherList(): WeatherList {
        val json = sharedPref.getString("saved_weather", "") ?: ""
        return if (json.isNotEmpty()) {
            Gson().fromJson(json, WeatherList::class.java)
        } else {
            WeatherList()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        pagerAdapter = null
    }
}
