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


/**
 * A fragment representing a list of Items.
 */
class WeatherListFragment : Fragment() {

    private var weatherList: WeatherList = WeatherList()
    private lateinit var binding: FragmentWeatherListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            weatherList = Gson().fromJson(it.getString(RESPONSE), WeatherList::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherListBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = WeatherRecyclerViewAdapter(weatherList.list, context)
        }
    }

    companion object {

        // TODO: Customize parameter argument names
        const val RESPONSE = "response"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(response: String) =
            WeatherListFragment().apply {
                arguments = Bundle().apply {
                    putString(RESPONSE, response)
                }
            }
    }
}