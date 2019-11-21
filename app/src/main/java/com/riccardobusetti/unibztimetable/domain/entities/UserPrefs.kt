package com.riccardobusetti.unibztimetable.domain.entities

import com.riccardobusetti.unibztimetable.data.remote.WebSiteUrl

/**
 * Class which represents the user preferences of the app.
 *
 * @author Riccardo Busetti
 */
class UserPrefs(val prefs: Map<Pref, String> = mutableMapOf()) {

    /**
     * Enum containing all the user preferences with their corresponding key.
     *
     * @author Riccardo Busetti
     */
    enum class Pref(val key: String) {
        DEPARTMENT_ID("DEPARTMENT"),
        DEGREE_ID("DEGREE"),
        STUDY_PLAN_ID("STUDY_PLAN")
    }
}

/**
 * Safely gets a value from a map and returns the default url parameter value if the map
 * doesn't contain a specific preference.
 */
fun Map<UserPrefs.Pref, String>.safeGet(pref: UserPrefs.Pref) =
    this[pref] ?: WebSiteUrl.DEFAULT_URL_PARAM_VALUE