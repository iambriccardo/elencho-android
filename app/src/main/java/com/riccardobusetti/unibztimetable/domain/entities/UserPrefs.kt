package com.riccardobusetti.unibztimetable.domain.entities

/**
 * Class which represents the user preferences of the app.
 *
 * @author Riccardo Busetti
 */
class UserPrefs(val prefs: Map<Pref, String>) {

    /**
     * Enum containing all the user preferences with their corresponding key.
     *
     * @author Riccardo Busetti
     */
    enum class Pref(val key: String) {
        DEPARTMENT_ID("DEPARTMENT"),
        DEGREE_ID("DEGREE"),
        STUDY_PLAN_ID("STUDY_PLAN"),
        USERNAME("USERNAME")
    }
}