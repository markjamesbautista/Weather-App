package com.example.weatherapplication.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherapplication.api.Response
import com.example.weatherapplication.ui.components.WeatherItem

@Composable
fun WeatherListScreen(
    weatherList: List<Response>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(
            items = weatherList,
            key = { item -> "${item.id}-${item.dt}" }
        ) { item ->
            WeatherItem(item = item)
        }
    }
}