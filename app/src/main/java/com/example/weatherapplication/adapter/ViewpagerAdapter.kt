package com.example.weatherapplication.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weatherapplication.fragment.WeatherListFragment

class ViewpagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private var currentDataJson: String = ""
    private var historyDataJson: String = ""

    fun updateData(current: String, history: String) {
        if (this.currentDataJson != current || this.historyDataJson != history) {
            this.currentDataJson = current
            this.historyDataJson = history
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> WeatherListFragment.newInstance(currentDataJson)
            else -> WeatherListFragment.newInstance(historyDataJson)
        }
    }

    override fun getItemId(position: Int): Long {
        return when (position) {
            0 -> (currentDataJson.hashCode().toLong() shl 32) or 0L
            else -> (historyDataJson.hashCode().toLong() shl 32) or 1L
        }
    }

    override fun containsItem(itemId: Long): Boolean {
        // Since we are only using 2 positions, check if the ID corresponds to one of them
        val pos = (itemId and 0xFFFFFFFFL).toInt()
        return pos in 0..1
    }
}
