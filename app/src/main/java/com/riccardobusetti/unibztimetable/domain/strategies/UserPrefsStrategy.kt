package com.riccardobusetti.unibztimetable.domain.strategies

import com.riccardobusetti.unibztimetable.domain.entities.UserPrefs

/**
 * Interface describing the behavior that any strategy should implement if they
 * want to fetch the user preferences.
 *
 * @author Riccardo Busetti
 */
interface UserPrefsStrategy : Strategy {

    /**
     * Saves the user preferences with a specific strategy.
     */
    fun putUserPrefs(userPrefs: UserPrefs)

    /**
     * Gets the preferences of the user from a data source which will be specified in the
     * strategy implementation.
     */
    fun getUserPrefs(): UserPrefs
}