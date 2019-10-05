package com.riccardobusetti.unibztimetable.ui.utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * Custom [ViewPager] component which disables the swiping between fragments.
 *
 * @author Riccardo Busetti
 */
class NonSwipeableViewPager(context: Context, attributeSet: AttributeSet?) :
    ViewPager(context, attributeSet) {

    override fun onTouchEvent(ev: MotionEvent?) = false

    override fun onInterceptTouchEvent(ev: MotionEvent?) = false
}