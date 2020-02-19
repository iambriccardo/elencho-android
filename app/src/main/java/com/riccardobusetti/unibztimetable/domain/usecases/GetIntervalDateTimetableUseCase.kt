package com.riccardobusetti.unibztimetable.domain.usecases

import com.riccardobusetti.unibztimetable.data.remote.WebSiteUrl
import com.riccardobusetti.unibztimetable.domain.entities.AppSection
import com.riccardobusetti.unibztimetable.domain.entities.Course
import com.riccardobusetti.unibztimetable.domain.entities.TimetableParams
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case which will manage the time machine function which gets the timetable between two
 * interval of dates.
 *
 * @author Riccardo Busetti
 */
class GetIntervalDateTimetableUseCase(
    private val timetableRepository: TimetableRepository
) : UseCase<TimetableParams, Flow<List<Course>>> {

    override fun execute(params: TimetableParams): Flow<List<Course>> {
        val webSiteUrl =
            WebSiteUrl.Builder()
                .useDeviceLanguage()
                .withDepartment(params.department)
                .withDegree(params.degree)
                .withStudyPlan(params.studyPlan)
                .fromDate(params.fromDate)
                .toDate(params.toDate)
                .atPage(params.page)
                .build()

        return timetableRepository.getTimetable(AppSection.TIME_MACHINE, webSiteUrl)
    }
}