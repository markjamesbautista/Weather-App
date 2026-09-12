package com.example.weatherapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherapplication.R
import com.example.weatherapplication.api.Response
import com.example.weatherapplication.ui.theme.SecondaryText
import com.example.weatherapplication.ui.theme.WeatherBackground
import com.example.weatherapplication.utils.WeatherTimeUtils

@Composable
fun WeatherItem(item: Response, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp)
            .background(WeatherBackground)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = WeatherTimeUtils.getFormattedDate(item.dt.toLong(), item.timezone),
                color = SecondaryText,
                fontSize = 12.sp
            )
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                WeatherIcon(item)
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "${item.name}, ${item.sys.country}",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "${item.main.temp}°C",
                color = Color.Black,
                fontSize = 14.sp
            )
            Text(
                text = "Sunrise: ${WeatherTimeUtils.getTime(item.sys.sunrise.toLong(), item.timezone)}",
                color = Color.Black,
                fontSize = 12.sp
            )
            Text(
                text = "Sunset: ${WeatherTimeUtils.getTime(item.sys.sunset.toLong(), item.timezone)}",
                color = Color.Black,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun WeatherIcon(item: Response) {
    val weatherMain = item.weather.firstOrNull()?.main ?: ""
    val isDay = WeatherTimeUtils.isDay(item.dt.toLong(), item.sys.sunrise, item.sys.sunset)

    when {
        weatherMain.contains("Rain", ignoreCase = true) -> {
            Image(
                painter = painterResource(id = R.drawable.ic_rainy),
                contentDescription = "Rainy",
                modifier = Modifier.size(30.dp)
            )
        }
        weatherMain.contains("Clear", ignoreCase = true) || 
        weatherMain.contains("Sun", ignoreCase = true) -> {
            if (!isDay) {
                Image(
                    painter = painterResource(id = R.drawable.ic_moon),
                    contentDescription = "Moon",
                    modifier = Modifier.size(30.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_sunny),
                    contentDescription = "Sunny",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
        else -> {
            val iconCode = item.weather.firstOrNull()?.icon
            AsyncImage(
                model = "https://openweathermap.org/img/wn/$iconCode@2x.png",
                contentDescription = weatherMain,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}