package com.jrodrigues.animatedpager

interface ViewSlider : AnimationListener, FirstScreenListener {

    fun goToPage(page: Int, newBackgroundIndex: Int?)
    fun forwardExitFlow()
    fun backwardExitFlow()

    interface TransitionListener {
        fun onTransitionEnterFinished()
        fun onTransitionForwardExitFlowFinished()
        fun onTransitionBackwardExitFlowFinished()
    }
}

interface FragmentPager {

    fun goToFragment(index: Int)
    fun getFragment(index: Int): AnimationFragment
}

interface BackgroundSwitcher {

    fun transitionForward(index: Int)
    fun transitionBackward(index: Int)
    fun setAnimationListener(listener: AnimationListener)

    interface AnimationListener {
        fun onBackgroundAnimationEnd()
    }
}
