package com.ankitkumar.securechatapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ankitkumar.securechatapplication.adapter.ViewPagerAdapter
import com.ankitkumar.securechatapplication.databinding.FragmentTabSwitchBinding
import com.google.android.material.tabs.TabLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TabSwitchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TabSwitchFragment : Fragment() {

    private lateinit var  binding: FragmentTabSwitchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tab_switch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTabSwitchBinding.bind(view)
        attachTabsToViewPager()
    }

    private fun attachTabsToViewPager() {
        val fragmentsList = mutableListOf(
            ChatListFragment.newInstance(),
            GroupListFragment.newInstance()
        )

        binding.viewpager.adapter = ViewPagerAdapter(
            childFragmentManager,
            lifecycle,
            fragmentsList
        )

        binding.tablayout.getTabAt(0)!!.select()
//        binding.tablayout.addOnTabSelectedListener(object  : TabLayout.OnTabSelectedListener{
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//            }
//
//        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TabSwitchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}