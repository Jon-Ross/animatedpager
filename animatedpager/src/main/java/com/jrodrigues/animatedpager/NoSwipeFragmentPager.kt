package com.jrodrigues.animatedpager

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NoSwipeFragmentPager(
    context: Context,
    attrs: AttributeSet?
) : ViewPager(context, attrs), FragmentPager {

    override fun goToFragment(index: Int) {
        setCurrentItem(index, true)
    }

    override fun getFragment(index: Int): AnimationFragment =
            (adapter as NoSwipeFragmentPagerAdapter).getItem(index)

    override fun onTouchEvent(event: MotionEvent?): Boolean = false
    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean = false
}
