package com.riccardobusetti.unibztimetable.domain.usecases

import android.util.Log
import com.riccardobusetti.unibztimetable.domain.entities.UserPrefsParams
import com.riccardobusetti.unibztimetable.domain.repositories.UserPrefsRepository

/**
 * Use case that will manage the saving of user preferences.
 *
 * @author Riccardo Busetti
 */
class PutUserPrefsUseCase(
    private val userPrefsRepository: UserPrefsRepository
) : UseCase<UserPrefsParams, Boolean> {

    companion object {
        private const val TAG = "PutUserPrefsUseCase"
    }

    override fun execute(params: UserPrefsParams) = try {
        userPrefsRepository.putUserPrefs(params.userPrefs)
        true
    } catch (e: Exception) {
        Log.d(TAG, "An error occurred while saving user preferences -> $e")

        false
    }
}