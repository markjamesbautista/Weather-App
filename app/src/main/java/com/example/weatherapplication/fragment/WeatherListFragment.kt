package com.example.weatherapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapplication.adapter.WeatherRecyclerViewAdapter
import com.example.weatherapplication.api.WeatherList
import com.example.weatherapplication.databinding.FragmentWeatherListBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherListFragment : Fragment() {

    private var _binding: FragmentWeatherListBinding? = null
    private val binding get() = _binding!!

    private var weatherList: WeatherList = WeatherList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val json = it.getString(ARG_WEATHER_JSON)
            if (!json.isNullOrEmpty()) {
                weatherList = Gson().fromJson(json, WeatherList::class.java)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val weatherAdapter = WeatherRecyclerViewAdapter()
        binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = weatherAdapter
        }
        weatherAdapter.submitList(weatherList.list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_WEATHER_JSON = "arg_weather_json"

        @JvmStatic
        fun newInstance(weatherJson: String) =
            WeatherListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_WEATHER_JSON, weatherJson)
                }
            }
    }
}
