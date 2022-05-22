package com.ankitkumar.securechatapplication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager,
                       lifecycle: Lifecycle,
                       private val fragmentsList: List<Fragment>) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
       return fragmentsList.size
    }

    override fun createFragment(position: Int): Fragment {
       return  fragmentsList[position]
    }
}