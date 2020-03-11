package com.riccardobusetti.unibztimetable.domain.usecases

import com.riccardobusetti.unibztimetable.domain.entities.params.Params
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository

/**
 * Use case which will delete all the data in the timetable table of the app database.
 *
 * @author Riccardo Busetti
 */
class DeleteLocalTimetableUseCase(
    private val timetableRepository: TimetableRepository
) : UseCase<Params?, Boolean> {


    override fun execute(params: Params?): Boolean {
        timetableRepository.deleteLocalTimetable()

        return true
    }
}