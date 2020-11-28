package com.jrodrigues.animatedpager

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class AnimationFragment(@LayoutRes layoutResId: Int) :
    Fragment(layoutResId), AnimatedView {

    protected var animationListener: AnimationListener? = null

    override fun onFirstEnter() {}
    override fun onEnter() {}
    override fun onFirstShowFully() {}
    override fun onOtherShowFully() {}

    fun setAnimatedListener(listener: AnimationListener?) {
        animationListener = listener
    }

    override fun onDetach() {
        animationListener = null
        super.onDetach()
    }
}

interface AnimatedView {
    fun onFirstEnter()
    fun onEnter()
    fun onFirstShowFully()
    fun onOtherShowFully()
    fun onTransitionForwardEnter()
    fun onTransitionBackwardEnter()
    fun onTransitionForwardExit()
    fun onTransitionBackwardExit()
}

interface AnimationListener {
    fun onFinishTransitionEnter()
    fun onFinishTransitionForwardsExit()
    fun onFinishTransitionBackwardsExit()
}

interface FirstScreenListener {
    fun onFirstScreenViewCreated()
}
