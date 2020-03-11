package com.riccardobusetti.unibztimetable.domain.usecases

import com.riccardobusetti.unibztimetable.data.remote.WebSiteUrl
import com.riccardobusetti.unibztimetable.domain.entities.AppSection
import com.riccardobusetti.unibztimetable.domain.entities.Course
import com.riccardobusetti.unibztimetable.domain.entities.params.TimetableParams
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository
import kotlinx.coroutines.flow.Flow


/**
 * Use case which will manage the next 7 days timetable that is responsible of
 * showing to the user the timetable of the next 7 days.
 *
 * @author Riccardo Busetti
 */
class GetNext7DaysTimetableUseCase(
    private val timetableRepository: TimetableRepository
) : UseCase<TimetableParams, Flow<List<Course>>> {

    override fun execute(params: TimetableParams): Flow<List<Course>> {
        val webSiteUrl =
            WebSiteUrl.Builder()
                .useDeviceLanguage()
                .withDepartment(params.department)
                .withDegree(params.degree)
                .withStudyPlan(params.studyPlan)
                .fromTomorrow()
                .toNext7Days()
                .atPage(params.page)
                .build()

        return timetableRepository.getTimetable(AppSection.NEXT_7_DAYS, webSiteUrl)
    }
}