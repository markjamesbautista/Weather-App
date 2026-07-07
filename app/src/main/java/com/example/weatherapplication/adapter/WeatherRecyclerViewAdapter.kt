package com.example.weatherapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapplication.R
import com.example.weatherapplication.api.Response
import com.example.weatherapplication.databinding.AdapterWeatherItemListBinding
import com.example.weatherapplication.utils.WeatherTimeUtils

class WeatherRecyclerViewAdapter : 
    ListAdapter<Response, WeatherRecyclerViewAdapter.ViewHolder>(WeatherDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AdapterWeatherItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: AdapterWeatherItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Response) {
            binding.apply {
                txtLocation.text = "${item.name}, ${item.sys.country}"
                txtTemp.text = "${item.main.temp}°C"
                
                txtDate.text = WeatherTimeUtils.getFormattedDate(item.dt.toLong(), item.timezone)
                txtSunrise.text = "Sunrise: ${WeatherTimeUtils.getTime(item.sys.sunrise.toLong(), item.timezone)}"
                txtSunset.text = "Sunset: ${WeatherTimeUtils.getTime(item.sys.sunset.toLong(), item.timezone)}"

                val weatherMain = item.weather.firstOrNull()?.main ?: ""
                
                // Precise day/night logic based on sunrise/sunset timestamps
                val isDay = WeatherTimeUtils.isDay(item.dt.toLong(), item.sys.sunrise, item.sys.sunset)

                when {
                    weatherMain.contains("Rain", ignoreCase = true) -> {
                        icon.setImageResource(R.drawable.ic_rainy)
                    }
                    weatherMain.contains("Clear", ignoreCase = true) || 
                    weatherMain.contains("Sun", ignoreCase = true) -> {
                        if (!isDay) {
                            icon.setImageResource(R.drawable.ic_moon)
                        } else {
                            icon.setImageResource(R.drawable.ic_sunny)
                        }
                    }
                    else -> {
                        val iconCode = item.weather.firstOrNull()?.icon
                        val imageUrl = "https://openweathermap.org/img/wn/$iconCode@2x.png"
                        Glide.with(root.context).load(imageUrl).into(icon)
                    }
                }
            }
        }
    }

    class WeatherDiffCallback : DiffUtil.ItemCallback<Response>() {
        override fun areItemsTheSame(oldItem: Response, newItem: Response): Boolean {
            // For history, items are the same if it's the same city at the same time
            return oldItem.id == newItem.id && oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(oldItem: Response, newItem: Response): Boolean {
            return oldItem == newItem
        }
    }
}
