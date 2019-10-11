package com.riccardobusetti.unibztimetable.domain.repositories

import com.riccardobusetti.unibztimetable.domain.entities.UserPrefs
import com.riccardobusetti.unibztimetable.domain.strategies.SharedPreferencesUserPrefsStrategy

/**
 * Repository implementation which will get the user preferences from the shared preferences.
 *
 * This implementation is using only the shared preferences as a strategy, but we can add in future
 * remote preferences or any data source we want.
 *
 * @author Riccardo Busetti
 */
class UserPrefsRepository(
    private val sharedPreferencesUserPrefsStrategy: SharedPreferencesUserPrefsStrategy
) : Repository {

    fun putUserPrefs(userPrefs: UserPrefs) {
        sharedPreferencesUserPrefsStrategy.putUserPrefs(userPrefs)
    }

    fun getUserPrefs() = sharedPreferencesUserPrefsStrategy.getUserPrefs()
}