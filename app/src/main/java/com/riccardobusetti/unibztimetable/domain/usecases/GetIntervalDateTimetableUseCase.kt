package com.riccardobusetti.unibztimetable.domain.usecases

import com.riccardobusetti.unibztimetable.domain.entities.Day
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository
import com.riccardobusetti.unibztimetable.network.WebSiteLink

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
    fun getTimetable(
        department: String,
        degree: String,
        academicYear: String,
        fromDate: String,
        toDate: String,
        page: String
    ): List<Day> {
        val webSiteLink =
            WebSiteLink.Builder()
                .useDeviceLanguage()
                .withDepartment(department)
                .withDegree(degree)
                .withAcademicYear(academicYear)
                .fromDate(fromDate)
                .toDate(toDate)
                .atPage(page)
                .build()

        return timetableRepository.getTimetable(webSiteLink)
    }
}