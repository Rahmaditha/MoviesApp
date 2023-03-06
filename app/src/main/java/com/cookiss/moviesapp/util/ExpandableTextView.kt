package com.cookiss.moviesapp.util

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation

object ExpandableTextView {

    fun View.expand(duration: Long) {
        val initialHeight = this.measuredHeight
        val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec((this.parent as View).width, View.MeasureSpec.EXACTLY)
        val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        this.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
        val targetHeight = this.measuredHeight

        this.layoutParams.height = initialHeight
        this.visibility = View.VISIBLE
        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                this@expand.layoutParams.height = if (interpolatedTime == 1.0f) ViewGroup.LayoutParams.WRAP_CONTENT else (initialHeight + ((targetHeight - initialHeight) * interpolatedTime)).toInt()
                this@expand.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }


        a.duration = duration
        this.startAnimation(a)
    }

    fun View.collapse(duration: Long) {
        val initialHeight = this.measuredHeight
        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                if (interpolatedTime == 1.0f) {
                    this@collapse.visibility = View.GONE
                } else {
                    this@collapse.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                    this@collapse.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        a.duration = duration
        this@collapse.startAnimation(a)
    }
}