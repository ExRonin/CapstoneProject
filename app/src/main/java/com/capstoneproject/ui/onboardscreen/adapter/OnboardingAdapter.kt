@file:Suppress("DEPRECATION")

package com.capstoneproject.ui.onboardscreen.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.capstoneproject.R
import com.capstoneproject.ui.onboardscreen.OnboardingFragment

class OnboardingAdapter(
    manager: FragmentManager,
    private val context: Context
) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return NUM_ITEMS
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> OnboardingFragment.newInstance(
                context.resources.getString(R.string.title_onboarding),
                context.resources.getString(R.string.description_onboarding_1),
                R.drawable.frame7
            )
            1 -> OnboardingFragment.newInstance(
                context.resources.getString(R.string.title_onboarding_2),
                context.resources.getString(R.string.description_onboarding_2),
                R.drawable.frame7
            )
            2 -> OnboardingFragment.newInstance(
                context.resources.getString(R.string.title_onboarding_3),
                context.resources.getString(R.string.description_onboarding_3),
                R.drawable.frame7
            )
            else -> OnboardingFragment.newInstance(
                context.resources.getString(R.string.title_onboarding),
                context.resources.getString(R.string.description_onboarding_1),
                R.drawable.frame7
            )
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return "Page $position"
    }

    companion object {
        private const val NUM_ITEMS = 3
    }
}