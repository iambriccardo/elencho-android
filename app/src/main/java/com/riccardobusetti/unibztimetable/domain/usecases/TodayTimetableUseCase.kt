package com.riccardobusetti.unibztimetable.domain.usecases

import com.riccardobusetti.unibztimetable.domain.entities.Day
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository
import com.riccardobusetti.unibztimetable.network.WebSiteLink

/**
 * Use case which will manage the today timetable that is responsible of
 * showing to the user the timetable of the current day.
 *
 * @author Riccardo Busetti
 */
class TodayTimetableUseCase(
    private val timetableRepository: TimetableRepository
) : UseCase {

    /**
     * Gets the timetable of today.
     */
    fun getTodayTimetable(
        department: String,
        degree: String,
        academicYear: String,
        page: String
    ): List<Day> {
        val webSiteLink = WebSiteLink.Builder()
            .useDeviceLanguage()
            .withDepartment(department)
            .withDegree(degree)
            .withAcademicYear(academicYear)
            .onlyToday()
            .atPage(page)
            .build()

        return timetableRepository.getTimetable(webSiteLink)
    }
}