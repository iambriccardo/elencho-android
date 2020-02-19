package com.riccardobusetti.unibztimetable.domain.usecases

import com.riccardobusetti.unibztimetable.domain.entities.AppSection
import com.riccardobusetti.unibztimetable.domain.entities.Course
import com.riccardobusetti.unibztimetable.domain.entities.Params
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository

/**
 * Use case that will fetch the local timetable of the today's app section.
 */
class GetTodaysLocalTimetableUseCase(
    private val timetableRepository: TimetableRepository
) : UseCase<Params?, List<Course>> {

    override fun execute(params: Params?) = timetableRepository.getLocalTimetable(AppSection.TODAY)
}