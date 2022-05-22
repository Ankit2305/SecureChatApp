package com.ankitkumar.securechatapplication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {

    private val fragmentList =  ArrayList<Fragment>()
    private val fragmentTitle = ArrayList<String>()
    override fun getCount(): Int {
        return  fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    fun addFragment( fragment: Fragment ,  title :String){
        fragmentList.add(fragment)
        fragmentTitle.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return  fragmentTitle[position]
    }
}