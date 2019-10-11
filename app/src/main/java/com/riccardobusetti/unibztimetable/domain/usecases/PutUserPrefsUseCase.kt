package com.riccardobusetti.unibztimetable.domain.usecases

import com.riccardobusetti.unibztimetable.domain.entities.UserPrefs
import com.riccardobusetti.unibztimetable.domain.repositories.UserPrefsRepository

/**
 * Use case that will manage the saving of user preferences.
 *
 * @author Riccardo Busetti
 */
class PutUserPrefsUseCase(
    private val userPrefsRepository: UserPrefsRepository
) : UseCase {

    fun putUserPrefs(departmentId: String, degreeId: String, studyPlanId: String) {
        putUserPrefs(
            UserPrefs(
                mapOf(
                    UserPrefs.Pref.DEPARTMENT_ID to departmentId,
                    UserPrefs.Pref.DEGREE_ID to degreeId,
                    UserPrefs.Pref.STUDY_PLAN_ID to studyPlanId
                )
            )
        )
    }

    fun putUserPrefs(userPrefs: UserPrefs) {
        userPrefsRepository.putUserPrefs(userPrefs)
    }
}