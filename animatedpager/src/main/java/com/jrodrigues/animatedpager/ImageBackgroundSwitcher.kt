package com.jrodrigues.animatedpager

import android.view.animation.Animation
import android.widget.ImageSwitcher

class ImageBackgroundSwitcher(
    private val imageSwitcher: ImageSwitcher,
    private val images: List<Int>,
    private val slideBackwardCurrentAnim: Animation,
    private val slideBackwardPrevAnim: Animation,
    private val slideForwardCurrentAnim: Animation,
    private val slideForwardNextAnim: Animation
) : BackgroundSwitcher {

    private var animationListener: BackgroundSwitcher.AnimationListener? = null

    init {
        slideForwardNextAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(p0: Animation?) {
                animationListener?.onBackgroundAnimationEnd()
            }
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationStart(p0: Animation?) {}
        })
        slideBackwardPrevAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(p0: Animation?) {
                animationListener?.onBackgroundAnimationEnd()
            }
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationStart(p0: Animation?) {}
        })
    }

    override fun transitionForward(index: Int) {
        imageSwitcher.inAnimation = slideForwardNextAnim
        imageSwitcher.outAnimation = slideForwardCurrentAnim
        imageSwitcher.setImageResource(images[index])
    }

    override fun transitionBackward(index: Int) {
        imageSwitcher.inAnimation = slideBackwardPrevAnim
        imageSwitcher.outAnimation = slideBackwardCurrentAnim
        imageSwitcher.setImageResource(images[index])
    }

    override fun setAnimationListener(listener: BackgroundSwitcher.AnimationListener) {
        animationListener = listener
    }
}
