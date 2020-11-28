package com.jrodrigues.animatedpager

internal class ViewSliderServiceLocator(
    private val view: ViewSliderContract.View,
    private val pageCount: Int,
    private val imagesCount: Int,
    private val firstShownPage: Int,
    private val firstShownImage: Int
) {

    fun getPresenter(): ViewSliderContract.Presenter =
        DynamicViewSliderPresenter(
            view,
            pageCount,
            imagesCount,
            firstShownPage,
            firstShownImage
        )

}
