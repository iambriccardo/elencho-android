package com.riccardobusetti.unibztimetable.utils

import com.riccardobusetti.unibztimetable.domain.entities.UserPrefs

object URLUtils {

    fun parseURL(url: String): Map<UserPrefs.Pref, String> {
        // TODO: implement the url parsing.
        return mapOf(
            UserPrefs.Pref.DEPARTMENT_ID to "113",
            UserPrefs.Pref.DEGREE_ID to "1234",
            UserPrefs.Pref.STUDY_PLAN_ID to "1324"
        )
    }
}