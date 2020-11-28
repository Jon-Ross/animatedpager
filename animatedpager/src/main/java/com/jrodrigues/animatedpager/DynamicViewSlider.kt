package com.jrodrigues.animatedpager

import android.os.Handler

class DynamicViewSlider(
    private val fragmentPager: FragmentPager,
    private val backgroundSwitcher: BackgroundSwitcher,
    pageCount: Int,
    imageCount: Int,
    firstShownPage: Int = 0,
    firstShownImage: Int = 0,
    private val transitionListener: ViewSlider.TransitionListener
) : ViewSlider, ViewSliderContract.View, BackgroundSwitcher.AnimationListener {

    private val presenter: ViewSliderContract.Presenter =
            ViewSliderServiceLocator(
                    this,
                    pageCount,
                    imageCount,
                    firstShownPage,
                    firstShownImage
            ).getPresenter()

    init {
        backgroundSwitcher.setAnimationListener(this)
    }

    override fun onFirstScreenViewCreated() {
        presenter.onFirstScreenViewCreated()
    }

    override fun goToPage(page: Int, newBackgroundIndex: Int?) {
        presenter.onGoToPage(page, newBackgroundIndex)
    }

    override fun forwardExitFlow() {
        presenter.onForwardExitFlow()
    }

    override fun backwardExitFlow() {
        presenter.onBackwardExitFlow()
    }

    override fun onFinishTransitionEnter() {
        presenter.onFinishTransitionEnter()
    }

    override fun onFinishTransitionForwardsExit() {
        presenter.onFinishTransitionForwardsExit()
    }

    override fun onFinishTransitionBackwardsExit() {
        presenter.onFinishTransitionBackwardsExit()
    }

    override fun onBackgroundAnimationEnd() {
        presenter.onBackgroundAnimationEnd()
    }

    override fun transitionBackgroundForward(backgroundIndex: Int) {
        backgroundSwitcher.transitionForward(backgroundIndex)
    }

    override fun transitionBackgroundBackward(backgroundIndex: Int) {
        backgroundSwitcher.transitionBackward(backgroundIndex)
    }

    override fun transitionPager(page: Int) {
        fragmentPager.goToFragment(page)
    }

    override fun transitionForwardEnter(page: Int) {
        val fragment = getFragmentAtPosition(page)
        fragment.onTransitionForwardEnter()
    }

    override fun transitionForwardEnterAfterDelay(page: Int, delay: Long) {
        Handler().postDelayed({
            val fragment = getFragmentAtPosition(page)
            fragment.onTransitionForwardEnter()
        }, delay)
    }

    override fun transitionBackwardEnter(page: Int) {
        val fragment = getFragmentAtPosition(page)
        fragment.onTransitionBackwardEnter()
    }

    override fun transitionBackwardEnterAfterDelay(page: Int, delay: Long) {
        Handler().postDelayed({
            val fragment = getFragmentAtPosition(page)
            fragment.onTransitionBackwardEnter()
        }, delay)
    }

    override fun transitionForwardExit(page: Int) {
        val fragment = getFragmentAtPosition(page)
        fragment.onTransitionForwardExit()
    }

    override fun transitionBackwardExit(page: Int) {
        val fragment = getFragmentAtPosition(page)
        fragment.onTransitionBackwardExit()
    }

    override fun notifyPageShowFirstFully(page: Int) {
        val fragment = getFragmentAtPosition(page)
        fragment.onFirstShowFully()
    }

    override fun notifyPageShowOtherFully(page: Int) {
        val fragment = getFragmentAtPosition(page)
        fragment.onOtherShowFully()
    }

    override fun notifyPageFirstEnter(page: Int) {
        val fragment = getFragmentAtPosition(page)
        fragment.onFirstEnter()
    }

    override fun notifyPageEnterAfterDelay(page: Int, delay: Long) {
        Handler().postDelayed({
            val fragment = getFragmentAtPosition(page)
            fragment.onEnter()
        }, delay)
    }

    override fun notifyPageEnter(page: Int) {
        val fragment = getFragmentAtPosition(page)
        fragment.onEnter()
    }

    override fun notifyPageFirstEnterAfterDelay(page: Int, delay: Long) {
        Handler().postDelayed({
            val fragment = getFragmentAtPosition(page)
            fragment.onFirstEnter()
        }, delay)
    }

    override fun notifyTransitionEnterFinished() {
        transitionListener.onTransitionEnterFinished()
    }

    override fun notifyTransitionForwardExitFlowFinished() {
        transitionListener.onTransitionForwardExitFlowFinished()
    }

    override fun notifyTransitionBackwardExitFlowFinished() {
        transitionListener.onTransitionBackwardExitFlowFinished()
    }

    private fun getFragmentAtPosition(page: Int): AnimationFragment =
            fragmentPager.getFragment(page)
}
