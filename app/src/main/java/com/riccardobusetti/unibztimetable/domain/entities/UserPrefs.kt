package com.riccardobusetti.unibztimetable.domain.entities

import com.riccardobusetti.unibztimetable.data.remote.WebSiteUrl

/**
 * Class which represents the user preferences of the app.
 *
 * @author Riccardo Busetti
 */
class UserPrefs(val prefs: Map<Pref, String> = mutableMapOf()) {

    /**
     * Enum containing all the types of preferences.
     *
     * The mandatory preferences are the ones needed for the app to work, so without
     * them the app will not start because the configuration will be prompted.
     *
     * @author Riccardo Busetti
     */
    enum class PrefType {
        MANDATORY,
        NOT_MANDATORY
    }

    /**
     * Enum containing all the user preferences with their corresponding key.
     *
     * @author Riccardo Busetti
     */
    enum class Pref(val key: String, val type: PrefType) {
        DEPARTMENT_ID("DEPARTMENT", PrefType.MANDATORY),
        DEGREE_ID("DEGREE", PrefType.MANDATORY),
        STUDY_PLAN_ID("STUDY_PLAN", PrefType.MANDATORY),
        DAILY_NOTIFICATION_TIME("DAILY_NOTIFICATION_TIME", PrefType.NOT_MANDATORY)
    }
}

/**
 * Safely gets a value from a map and returns the default url parameter value if the map
 * doesn't contain a specific preference.
 */
fun Map<UserPrefs.Pref, String>.safeGet(
    pref: UserPrefs.Pref,
    fallbackValue: String = WebSiteUrl.DEFAULT_URL_PARAM_VALUE
) =
    this[pref] ?: fallbackValue

fun Map<UserPrefs.Pref, String>.onlyMandatory() =
    this.filter { it.key.type == UserPrefs.PrefType.MANDATORY }

fun Array<UserPrefs.Pref>.onlyMandatory() = this.filter { it.type == UserPrefs.PrefType.MANDATORY }