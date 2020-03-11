package com.riccardobusetti.unibztimetable.domain.usecases

import com.riccardobusetti.unibztimetable.domain.entities.UserPrefs
import com.riccardobusetti.unibztimetable.domain.entities.params.Params
import com.riccardobusetti.unibztimetable.domain.repositories.UserPrefsRepository

/**
 * Use case that will manage the user preferences.
 *
 * @author Riccardo Busetti
 */
class GetUserPrefsUseCase(
    private val userPrefsRepository: UserPrefsRepository
) : UseCase<Params?, UserPrefs> {

    override fun execute(params: Params?) = userPrefsRepository.getUserPrefs()
}