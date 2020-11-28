package com.jrodrigues.animatedpager

internal interface ViewSliderContract {

    interface View {
        fun transitionPager(page: Int)
        fun transitionForwardEnter(page: Int)
        fun transitionForwardEnterAfterDelay(page: Int, delay: Long)
        fun transitionBackwardEnter(page: Int)
        fun transitionBackwardEnterAfterDelay(page: Int, delay: Long)
        fun transitionForwardExit(page: Int)
        fun transitionBackwardExit(page: Int)
        fun notifyPageShowFirstFully(page: Int)
        fun notifyPageShowOtherFully(page: Int)
        fun notifyPageFirstEnter(page: Int)
        fun notifyPageEnter(page: Int)
        fun notifyPageFirstEnterAfterDelay(page: Int, delay: Long)
        fun notifyPageEnterAfterDelay(page: Int, delay: Long)
        fun transitionBackgroundForward(backgroundIndex: Int)
        fun transitionBackgroundBackward(backgroundIndex: Int)
        fun notifyTransitionForwardExitFlowFinished()
        fun notifyTransitionBackwardExitFlowFinished()
        fun notifyTransitionEnterFinished()
    }

    interface Presenter {
        fun onFirstScreenViewCreated()
        fun onGoToPage(page: Int, backgroundIndex: Int?)
        fun onForwardExitFlow()
        fun onBackwardExitFlow()
        fun onFinishTransitionForwardsExit()
        fun onFinishTransitionBackwardsExit()
        fun onBackgroundAnimationEnd()
        fun onFinishTransitionEnter()
    }
}
