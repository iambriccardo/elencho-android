package com.riccardobusetti.unibztimetable.utils

import android.app.Activity
import android.util.DisplayMetrics


object ScreenUtils {

    fun getScreenHeight(activity: Activity) = getDisplayMetrics(activity).heightPixels

    fun getScreenWidth(activity: Activity) = getDisplayMetrics(activity).widthPixels

    private fun getDisplayMetrics(activity: Activity): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)

        return displayMetrics
    }
}