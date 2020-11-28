package com.jrodrigues.animatedpager

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Before
import org.junit.Test

class DynamicViewSliderPresenterTest {

    private companion object {
        const val PAGE_COUNT = 4
        const val IMAGE_COUNT = 3
        const val INITIAL_PAGE = 0
        const val INITIAL_IMAGE = 0
        const val BELOW_BOUND_IMAGE = INITIAL_IMAGE - 1
        const val BELOW_BOUND_PAGE = INITIAL_PAGE - 1
        const val FRAGMENT_ANIMATION_DELAY = 100L
    }

    @MockK
    private lateinit var view: ViewSliderContract.View

    private lateinit var presenter: ViewSliderContract.Presenter

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        presenter = DynamicViewSliderPresenter(
            view,
            PAGE_COUNT,
            IMAGE_COUNT,
            INITIAL_PAGE,
            INITIAL_IMAGE
        )
        presenter.onFirstScreenViewCreated()
        presenter.onFinishTransitionEnter()
    }

    @Test
    fun `Given page same as current, When onGoToPage, Then do nothing`() {
        // given
        val toPage = INITIAL_PAGE
        val toImage = INITIAL_IMAGE

        // when
        presenter.onGoToPage(toPage, toImage)

        // then
        verifySequence {
            verifyInitialCalls()
        }
    }

    @Test
    fun `Given page forward above bounds, When going to next page, Then notify that flow finished forwards`() {
        // given
        val toPage = PAGE_COUNT
        val toImage = INITIAL_IMAGE
        presenter.onGoToPage(toPage, toImage)

        // when
        presenter.onFinishTransitionForwardsExit()

        // then
        verifySequence {
            verifyInitialCalls()

            view.transitionForwardExit(INITIAL_PAGE)
            view.notifyTransitionForwardExitFlowFinished()
        }
    }

    @Test
    fun `Given page backward below bounds, When going to next page, Then notify that flow finished backwards`() {
        // given
        val toPage = BELOW_BOUND_PAGE
        val toImage = INITIAL_IMAGE
        presenter.onGoToPage(toPage, toImage)

        // when
        presenter.onFinishTransitionBackwardsExit()

        // then
        verifySequence {
            verifyInitialCalls()

            view.transitionBackwardExit(INITIAL_PAGE)
            view.notifyTransitionBackwardExitFlowFinished()
        }
    }

    @Test
    fun `Given page forward with different background, When going to next page, Then transition page and background forward`() {
        // given
        val toPage = INITIAL_PAGE + 1
        val toImage = INITIAL_IMAGE + 1

        // when
        goForwardToPageWithDifferentBackground(toPage, toImage)

        // then
        verifySequence {
            verifyInitialCalls()
            verifyTransitionPageAndBackgroundForwardSequenceCalls(INITIAL_PAGE, toPage, toImage)
        }
    }

    @Test
    fun `Given page forward with no background, When going to next page, Then transition only page forward`() {
        // given
        val toPage = INITIAL_PAGE + 1
        val toImage = null

        // when
        presenter.onGoToPage(toPage, toImage)
        presenter.onFinishTransitionForwardsExit()
        presenter.onFinishTransitionEnter()

        // then
        verifySequence {
            verifyInitialCalls()

            view.transitionForwardExit(INITIAL_PAGE)
            view.transitionPager(toPage)
            view.transitionForwardEnterAfterDelay(toPage, FRAGMENT_ANIMATION_DELAY)
            view.notifyPageFirstEnter(toPage)
            view.notifyPageEnter(toPage)
            view.notifyPageShowFirstFully(toPage)
            view.notifyTransitionEnterFinished()
        }
        verify(exactly = 0) { view.transitionBackgroundForward(any()) }
    }

    @Test
    fun `Given page forward with background out of bounds, When going to next page, Then transition only page forward`() {
        // given
        val toPage = INITIAL_PAGE + 1
        val toImage = BELOW_BOUND_IMAGE

        // when
        presenter.onGoToPage(toPage, toImage)
        presenter.onFinishTransitionForwardsExit()
        presenter.onFinishTransitionEnter()

        // then
        verifySequence {
            verifyInitialCalls()

            view.transitionForwardExit(INITIAL_PAGE)
            view.transitionPager(toPage)
            view.transitionForwardEnterAfterDelay(toPage, FRAGMENT_ANIMATION_DELAY)
            view.notifyPageFirstEnter(toPage)
            view.notifyPageEnter(toPage)
            view.notifyPageShowFirstFully(toPage)
            view.notifyTransitionEnterFinished()
        }
        verify(exactly = 0) { view.transitionBackgroundForward(any()) }
    }

    @Test
    fun `Given page forward with same background, When going to next page, Then transition only page forward`() {
        // given
        val toPage = INITIAL_PAGE + 1
        val toImage = INITIAL_IMAGE

        // when
        presenter.onGoToPage(toPage, toImage)
        presenter.onFinishTransitionForwardsExit()
        presenter.onFinishTransitionEnter()

        // then
        verifySequence {
            verifyInitialCalls()

            view.transitionForwardExit(INITIAL_PAGE)
            view.transitionPager(toPage)
            view.transitionForwardEnterAfterDelay(toPage, FRAGMENT_ANIMATION_DELAY)
            view.notifyPageFirstEnter(toPage)
            view.notifyPageEnter(toPage)
            view.notifyPageShowFirstFully(toPage)
            view.notifyTransitionEnterFinished()
        }
        verify(exactly = 0) { view.transitionBackgroundForward(any()) }
    }

    @Test
    fun `Given page backward with different background, When going to next page, Then transition page and background backward`() {
        // given
        val fromPage = INITIAL_PAGE + 1
        val fromImage = INITIAL_IMAGE + 1
        goForwardToPageWithDifferentBackground(fromPage, fromImage)

        val toPage = INITIAL_PAGE
        val toImage = INITIAL_IMAGE

        // when
        goBackwardToPageWithDifferentBackground(toPage, toImage)

        // then
        verifySequence {
            verifyInitialCalls()
            verifyTransitionPageAndBackgroundForwardSequenceCalls(INITIAL_PAGE, fromPage, fromImage)

            verifyTransitionPageAndBackgroundBackwardSequenceCalls(fromPage, toPage, toImage)
        }
    }

    @Test
    fun `Given page backward with same background, When going to next page, Then transition only page backward`() {
        // given
        val fromPage = INITIAL_PAGE + 1
        val fromImage = INITIAL_IMAGE + 1
        goForwardToPageWithDifferentBackground(fromPage, fromImage)

        val toPage = INITIAL_PAGE

        // when
        presenter.onGoToPage(toPage, fromImage)
        presenter.onFinishTransitionBackwardsExit()
        presenter.onFinishTransitionEnter()

        // then
        verifySequence {
            verifyInitialCalls()
            verifyTransitionPageAndBackgroundForwardSequenceCalls(INITIAL_PAGE, fromPage, fromImage)

            view.transitionBackwardExit(fromPage)
            view.transitionPager(toPage)
            view.transitionBackwardEnterAfterDelay(toPage, FRAGMENT_ANIMATION_DELAY)
            view.notifyPageEnterAfterDelay(toPage, FRAGMENT_ANIMATION_DELAY)
            view.notifyPageShowOtherFully(toPage)
            view.notifyTransitionEnterFinished()
        }
        verify(exactly = 0) { view.transitionBackgroundBackward(any()) }
    }

    @Test
    fun `Given onForwardExitFlow, When onFinishTransitionForwardsExit, Then notify that flow finished`() {
        // given
        presenter.onForwardExitFlow()

        // when
        presenter.onFinishTransitionForwardsExit()

        // then
        verifySequence {
            verifyInitialCalls()
            view.transitionForwardExit(INITIAL_PAGE)
            view.notifyTransitionForwardExitFlowFinished()
        }
    }

    @Test
    fun `Given onBackwardExitFlow, When onFinishTransitionBackwardsExit, Then notify that flow finished`() {
        // given
        presenter.onBackwardExitFlow()

        // when
        presenter.onFinishTransitionBackwardsExit()

        // then
        verifySequence {
            verifyInitialCalls()
            view.transitionBackwardExit(INITIAL_PAGE)
            view.notifyTransitionBackwardExitFlowFinished()
        }
    }

    @Test
    fun `Given have already gone forward to page, When go forward again to same page, Then notify page enter and shown fully again`() {
        // given
        val toPage = INITIAL_PAGE + 1
        val toImage = INITIAL_IMAGE + 1
        goForwardToPageWithDifferentBackground(toPage, toImage)

        val fromPage = INITIAL_PAGE
        val fromImage = INITIAL_IMAGE
        goBackwardToPageWithDifferentBackground(fromPage, fromImage)

        // when
        goForwardToPageWithDifferentBackground(toPage, toImage)

        // then
        verifySequence {
            verifyInitialCalls()
            verifyTransitionPageAndBackgroundForwardSequenceCalls(INITIAL_PAGE, toPage, toImage)
            verifyTransitionPageAndBackgroundBackwardSequenceCalls(toPage, fromPage, fromImage)

            view.transitionForwardExit(fromPage)
            view.transitionPager(toPage)
            view.transitionBackgroundForward(toImage)
            view.transitionForwardEnter(toPage)
            view.notifyPageEnter(toPage)
            view.notifyPageShowOtherFully(toPage)
            view.notifyTransitionEnterFinished()
        }
    }

    @Test
    fun `Given have already gone forward to page, When go forward again to different page, Then notify both pages shown fully first`() {
        // given
        val toPage = INITIAL_PAGE + 1
        val toImage = INITIAL_IMAGE + 1
        goForwardToPageWithDifferentBackground(toPage, toImage)

        val toNextPage = toPage + 1
        val toNextImage = toImage + 1

        // when
        goForwardToPageWithDifferentBackground(toNextPage, toNextImage)

        // then
        verifySequence {
            verifyInitialCalls()
            verifyTransitionPageAndBackgroundForwardSequenceCalls(INITIAL_PAGE, toPage, toImage)

            verifyTransitionPageAndBackgroundForwardSequenceCalls(toPage, toNextPage, toNextImage)
        }
    }

    private fun verifyInitialCalls() {
        view.transitionPager(INITIAL_PAGE)
        view.transitionForwardEnterAfterDelay(INITIAL_PAGE, FRAGMENT_ANIMATION_DELAY)
        view.notifyPageFirstEnterAfterDelay(INITIAL_PAGE, FRAGMENT_ANIMATION_DELAY)
        view.notifyPageEnterAfterDelay(INITIAL_PAGE, FRAGMENT_ANIMATION_DELAY)
        view.notifyPageShowFirstFully(INITIAL_PAGE)
        view.notifyTransitionEnterFinished()
    }

    private fun goForwardToPageWithDifferentBackground(page: Int, image: Int) {
        presenter.onGoToPage(page, image)
        presenter.onFinishTransitionForwardsExit()
        presenter.onBackgroundAnimationEnd()
        presenter.onFinishTransitionEnter()
    }

    private fun goBackwardToPageWithDifferentBackground(page: Int, image: Int) {
        presenter.onGoToPage(page, image)
        presenter.onFinishTransitionBackwardsExit()
        presenter.onBackgroundAnimationEnd()
        presenter.onFinishTransitionEnter()
    }

    private fun verifyTransitionPageAndBackgroundForwardSequenceCalls(fromPage: Int, toPage: Int, toImage: Int) {
        view.transitionForwardExit(fromPage)
        view.transitionPager(toPage)
        view.transitionBackgroundForward(toImage)
        view.transitionForwardEnter(toPage)
        view.notifyPageFirstEnter(toPage)
        view.notifyPageEnter(toPage)
        view.notifyPageShowFirstFully(toPage)
        view.notifyTransitionEnterFinished()
    }

    private fun verifyTransitionPageAndBackgroundBackwardSequenceCalls(fromPage: Int, toPage: Int, toImage: Int) {
        view.transitionBackwardExit(fromPage)
        view.transitionPager(toPage)
        view.transitionBackgroundBackward(toImage)
        view.transitionBackwardEnter(toPage)
        view.notifyPageEnter(toPage)
        view.notifyPageShowOtherFully(toPage)
        view.notifyTransitionEnterFinished()
    }
}
