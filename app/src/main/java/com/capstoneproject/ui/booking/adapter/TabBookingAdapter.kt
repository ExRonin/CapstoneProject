package com.capstoneproject.ui.booking.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstoneproject.ui.listads.ListAdsFragment
import com.capstoneproject.ui.listbooking.ListBookingFragment
import com.capstoneproject.ui.listshowads.ListShowAdsFragment

class TabBookingAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = ListBookingFragment()
            1 -> fragment = ListAdsFragment()
            2 -> fragment = ListShowAdsFragment()

        }
        return fragment as Fragment
    }

}