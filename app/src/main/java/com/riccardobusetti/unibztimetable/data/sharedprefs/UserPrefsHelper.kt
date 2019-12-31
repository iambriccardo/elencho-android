package com.riccardobusetti.unibztimetable.data.sharedprefs

import android.content.Context
import android.content.SharedPreferences

/**
 * Helper class which will manage the user preferences by using the shared preferences.
 *
 * @author Riccardo Busetti
 */
class UserPrefsHelper(context: Context) {

    companion object {
        private const val PREFS_FILE_NAME = "USER_PREFS"
        private const val DEFAULT_VALUE = ""
    }

    private var sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
    }

    fun putString(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            commit()
        }
    }

    fun getString(key: String): String? {
        return if (sharedPreferences.contains(key))
            sharedPreferences.getString(key, DEFAULT_VALUE) else null
    }

    fun removeString(key: String) {
        with(sharedPreferences.edit()) {
            remove(key)
            commit()
        }
    }
}