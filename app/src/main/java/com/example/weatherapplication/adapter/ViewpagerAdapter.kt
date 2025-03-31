package com.example.weatherapplication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewpagerAdapter(
    fragment: Fragment,
    val fragment1: Fragment,
    val fragment2: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        return when(position) {
            1 -> fragment2
            else -> fragment1
        }
    }
}