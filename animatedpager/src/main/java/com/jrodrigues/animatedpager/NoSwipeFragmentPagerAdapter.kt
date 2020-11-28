package com.jrodrigues.animatedpager

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

abstract class NoSwipeFragmentPagerAdapter(
    fm: FragmentManager,
    behavior: Int
) : FragmentPagerAdapter(fm, behavior) {

    abstract override fun getItem(position: Int): AnimationFragment
}
