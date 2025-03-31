package com.example.weatherapplication.adapter

import android.content.Context
import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.weatherapplication.api.Response
import com.example.weatherapplication.databinding.AdapterWeatherItemListBinding

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class WeatherRecyclerViewAdapter(
    private val values: List<Response>,
    private val context: Context
) : RecyclerView.Adapter<WeatherRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AdapterWeatherItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.txtLocation.text = "${item.name}, ${item.sys.country}"
        holder.txtTemp.text = "${item.main.temp}°C"
        holder.txtSunrise.text = "Sunrise: ${getTime(item.sys.sunrise)}"
        holder.txtSunset.text = "Sunset: ${getTime(item.sys.sunset)}"
        val image = "https://openweathermap.org/img/wn/${item.weather[0].icon}@2x.png"
        Glide.with(context).load(image).into(holder.ivIcon);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dateTime(time: Double, format: String = "K:mm a"): String {
        // parse the time zone
        val zoneId = ZoneId.of("Asia/Tokyo")
        // create a moment in time from the given timestamp (in seconds!)
        val instant = Instant.ofEpochSecond(time.toLong())
        // define a formatter using the given pattern and a Locale
        val formatter = DateTimeFormatter.ofPattern(format, Locale.ENGLISH)
        // then make the moment in time consider the zone and return the formatted String
        return instant.atZone(zoneId).format(formatter)
    }

    private fun getTime(time: Double): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateTime(time)
        } else {
            time.toString()
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: AdapterWeatherItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val txtLocation: TextView = binding.txtLocation
        val txtTemp: TextView = binding.txtTemp
        val txtSunrise: TextView = binding.txtSunrise
        val txtSunset: TextView = binding.txtSunset
        val ivIcon: ImageView = binding.icon
    }
}