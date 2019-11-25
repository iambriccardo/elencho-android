package com.riccardobusetti.unibztimetable.domain.usecases

import com.riccardobusetti.unibztimetable.data.remote.WebSiteUrl
import com.riccardobusetti.unibztimetable.domain.entities.AppSection
import com.riccardobusetti.unibztimetable.domain.entities.Kourse
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
) : UseCase {

    /**
     * Gets the timetable between an interval of dates.
     */
    fun getTimetableInInterval(
        department: String,
        degree: String,
        studyPlan: String,
        fromDate: String,
        toDate: String,
        page: String
    ): Flow<List<Kourse>> {
        val webSiteUrl =
            WebSiteUrl.Builder()
                .useDeviceLanguage()
                .withDepartment(department)
                .withDegree(degree)
                .withStudyPlan(studyPlan)
                .fromDate(fromDate)
                .toDate(toDate)
                .atPage(page)
                .build()

        return timetableRepository.getTimetable(AppSection.TIME_MACHINE, webSiteUrl)
    }
}