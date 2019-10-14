package com.riccardobusetti.unibztimetable.domain.entities

class UserPrefs(val prefs: Map<Pref, String>) {

    enum class Pref(val key: String) {
        DEPARTMENT_ID("DEPARTMENT"),
        DEGREE_ID("DEGREE"),
        STUDY_PLAN_ID("STUDY_PLAN"),
        USERNAME("USERNAME")
    }
}