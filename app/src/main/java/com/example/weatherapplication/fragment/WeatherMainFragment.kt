package com.example.weatherapplication.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weatherapplication.adapter.ViewpagerAdapter
import com.example.weatherapplication.api.Response
import com.example.weatherapplication.api.WeatherList
import com.example.weatherapplication.databinding.FragmentWeatherBinding
import com.example.weatherapplication.viewmodel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherMainFragment : Fragment() {

    private lateinit var binding: FragmentWeatherBinding
    private val viewModel: WeatherViewModel by viewModel()
    private val requestcode = 0
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private lateinit var sharedPref: SharedPreferences
    companion object {
        fun newInstance() = WeatherMainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        getLocation()
        sharedPref = requireActivity().getSharedPreferences("TESTER", Context.MODE_PRIVATE)
        viewModel.responseModel.collectOnStart(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                setUpViewPager(it)
            }
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                requestcode
            )
            false
        } else {
            true
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (isLocationPermissionGranted() && isLocationEnabled()) {
            fusedLocationClient.lastLocation.addOnCompleteListener {
                val location: Location?= it.result
                viewModel.getWeather(location?.latitude ?: 14.9968, location?.longitude ?: 121.1710)
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun setUpViewPager(response: List<Response>) {
        //create list of fragments

        val currentWeatherlist = WeatherList(
            response
        )
        val dataFromSharedPref = if (getDataFromSharedPref().isNotEmpty()){
            Gson().fromJson(getDataFromSharedPref(), WeatherList::class.java)
        } else {
            WeatherList()
        }

        val listRaw = response.toMutableList()
        dataFromSharedPref.list.forEach {
            listRaw.add(it)
        }

        val weatherList = WeatherList(
            listRaw
        )

        // initialize adapter
        binding.viewPager.apply {
            adapter = ViewpagerAdapter(
                this@WeatherMainFragment,
                WeatherListFragment.newInstance(Gson().toJson(currentWeatherlist)),
                WeatherListFragment.newInstance(Gson().toJson(weatherList))
            )

            offscreenPageLimit = 2
        }


        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Current Weather"
                1 -> "Weathers"
                else -> ""
            }
        }.attach()

        saveToSharedPref(weatherList)
    }

    private fun saveToSharedPref(weatherList: WeatherList) {
        val editor = sharedPref.edit()
        val value = Gson().toJson(weatherList)
        editor.putString("value", value)
        editor.apply()
    }

    private fun getDataFromSharedPref(): String {
        return sharedPref.getString("value", "") ?: ""
    }

    private fun <T> Flow<T>.collectOnStart(lifecycleOwner: LifecycleOwner, subscriber: suspend (T) -> Unit) {
        lifecycleOwner.launchOnStart {
            collect {
                subscriber.invoke(it)
            }
        }
    }

    private fun LifecycleOwner.launchOnStart(subscriber: suspend () -> Unit) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                subscriber.invoke()
            }
        }
    }
}