package com.riccardobusetti.unibztimetable.domain.usecases

import com.riccardobusetti.unibztimetable.data.remote.WebSiteUrl
import com.riccardobusetti.unibztimetable.domain.entities.AppSection
import com.riccardobusetti.unibztimetable.domain.entities.Course
import com.riccardobusetti.unibztimetable.domain.entities.params.TimetableParams
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Use case which will manage the today timetable that is responsible of
 * showing to the user the timetable of the current day.
 *
 * @author Riccardo Busetti
 */
class GetTodayTimetableUseCase(
    private val timetableRepository: TimetableRepository
) : UseCase<TimetableParams, Flow<List<Course>>> {

    override fun execute(params: TimetableParams): Flow<List<Course>> {
        val websiteUrl = WebSiteUrl.Builder()
            .useDeviceLanguage()
            .withDepartment(params.department)
            .withDegree(params.degree)
            .withStudyPlan(params.studyPlan)
            .onlyToday()
            .atPage(params.page)
            .build()

        return timetableRepository.getTimetable(AppSection.TODAY, websiteUrl).map { courses ->
            courses.filter { !it.isFinished() }
        }
    }
}