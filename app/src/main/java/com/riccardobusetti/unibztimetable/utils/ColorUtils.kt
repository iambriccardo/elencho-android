package com.riccardobusetti.unibztimetable.utils

import android.content.Context
import android.util.TypedValue

object ColorUtils {

    fun themeAttributeToResId(context: Context, themeAttributeId: Int, fallbackColorId: Int): Int {
        val outValue = TypedValue()
        val theme = context.theme
        val isResolved = theme.resolveAttribute(themeAttributeId, outValue, true)

        return if (isResolved) {
            outValue.resourceId
        } else {
            fallbackColorId
        }
    }
}