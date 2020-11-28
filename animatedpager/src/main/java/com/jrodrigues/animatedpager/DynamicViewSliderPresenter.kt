package com.jrodrigues.animatedpager

internal class DynamicViewSliderPresenter(
    private val view: ViewSliderContract.View,
    private val pageCount: Int,
    private val imageCount: Int,
    firstShownPage: Int,
    firstShownImage: Int
) : ViewSliderContract.Presenter {

    private companion object {
        const val FIRST_PAGE = 0
        const val FIRST_IMAGE = 0
        const val FRAGMENT_ANIMATION_DELAY = 100L
    }

    private var currentPage: Int = firstShownPage
    private var currentImage: Int = firstShownImage
    private val shownPagesFully: BooleanArray = BooleanArray(pageCount)
    private val shownPages: BooleanArray = BooleanArray(pageCount)

    private var step: Step? = Step.Next(firstShownPage, firstShownImage)

    override fun onFirstScreenViewCreated() {
        view.transitionPager(currentPage)
        view.transitionForwardEnterAfterDelay(currentPage, FRAGMENT_ANIMATION_DELAY)
        notifyPageEnterAfterDelay(currentPage)
    }

    override fun onGoToPage(page: Int, backgroundIndex: Int?) {
        when {
            page == currentPage -> return
            isOverUpperPageBound(page) -> {
                step = Step.Finish
                view.transitionForwardExit(currentPage)
            }
            isUnderLowerPageBound(page) -> {
                step = Step.Finish
                view.transitionBackwardExit(currentPage)
            }
            isGoingForward(page) -> {
                step = Step.Next(page, backgroundIndex)
                view.transitionForwardExit(currentPage)
            }
            isGoingBackwards(page) -> {
                step = Step.Next(page, backgroundIndex)
                view.transitionBackwardExit(currentPage)
            }
        }
    }

    override fun onForwardExitFlow() {
        step = Step.Finish
        view.transitionForwardExit(currentPage)
    }

    override fun onBackwardExitFlow() {
        step = Step.Finish
        view.transitionBackwardExit(currentPage)
    }

    override fun onFinishTransitionForwardsExit() {
        when (val nextStep = step) {
            null -> return
            is Step.Finish -> view.notifyTransitionForwardExitFlowFinished()
            is Step.Next -> {
                val toPage = nextStep.toPage
                view.transitionPager(toPage)
                val toImage = nextStep.toImage
                if (isInsideImageBounds(toImage) && toImage!! > currentImage) {
                    view.transitionBackgroundForward(toImage)
                } else {
                    currentPage = toPage
                    view.transitionForwardEnterAfterDelay(toPage, FRAGMENT_ANIMATION_DELAY)
                    notifyPageEnter(toPage)
                }
            }
        }
    }

    override fun onFinishTransitionBackwardsExit() {
        when (val nextStep = step) {
            null -> return
            is Step.Finish -> view.notifyTransitionBackwardExitFlowFinished()
            is Step.Next -> {
                val toPage = nextStep.toPage
                view.transitionPager(toPage)
                val toImage = nextStep.toImage
                if (isInsideImageBounds(toImage) && toImage!! < currentImage) {
                    view.transitionBackgroundBackward(toImage)
                } else {
                    currentPage = toPage
                    view.transitionBackwardEnterAfterDelay(toPage, FRAGMENT_ANIMATION_DELAY)
                    notifyPageEnterAfterDelay(toPage)
                }
            }
        }
    }

    override fun onBackgroundAnimationEnd() {
        val nextStep = step
        if (nextStep is Step.Next) {
            val toPage = nextStep.toPage
            if (isInsidePageBounds(toPage)) {
                if (isGoingForward(toPage)) {
                    view.transitionForwardEnter(toPage)
                } else {
                    view.transitionBackwardEnter(toPage)
                }
                notifyPageEnter(toPage)
                currentPage = toPage
                nextStep.toImage?.let {
                    currentImage = it
                }
            }
        }
    }

    override fun onFinishTransitionEnter() {
        val nextStep = step
        if (nextStep is Step.Next) {
            val toPage = nextStep.toPage
            if (isInsidePageBounds(toPage)) {
                notifyPageShowFully(currentPage)
                view.notifyTransitionEnterFinished()
            }
        }
    }

    private fun notifyPageEnter(page: Int) {
        if (!shownPages[page]) {
            view.notifyPageFirstEnter(page)
            shownPages[page] = true
        }
        view.notifyPageEnter(page)
    }

    private fun notifyPageEnterAfterDelay(page: Int) {
        if (!shownPages[page]) {
            view.notifyPageFirstEnterAfterDelay(page, FRAGMENT_ANIMATION_DELAY)
            shownPages[page] = true
        }
        view.notifyPageEnterAfterDelay(page, FRAGMENT_ANIMATION_DELAY)
    }

    private fun notifyPageShowFully(page: Int) {
        if (!shownPagesFully[page]) {
            view.notifyPageShowFirstFully(page)
            shownPagesFully[page] = true
        } else {
            view.notifyPageShowOtherFully(page)
        }
    }

    private fun isUnderLowerPageBound(page: Int): Boolean = page < FIRST_PAGE
    private fun isOverUpperPageBound(page: Int): Boolean = page >= pageCount
    private fun isInsidePageBounds(page: Int): Boolean =
            !isUnderLowerPageBound(page) && !isOverUpperPageBound(page)

    private fun isGoingBackwards(page: Int): Boolean =
            isInsidePageBounds(page) && page < currentPage

    private fun isGoingForward(page: Int): Boolean =
            isInsidePageBounds(page) && page > currentPage

    private fun isInsideImageBounds(imageIndex: Int?): Boolean =
            imageIndex != null && imageIndex >= FIRST_IMAGE && imageIndex < imageCount
}

private sealed class Step {

    data class Next(
        val toPage: Int,
        val toImage: Int?
    ) : Step()

    object Finish : Step()
}
