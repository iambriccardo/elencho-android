package com.riccardobusetti.unibztimetable.domain.usecases

import com.riccardobusetti.unibztimetable.data.remote.WebSiteUrl
import com.riccardobusetti.unibztimetable.domain.entities.AppSection
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository

/**
 * Use case that will manage the update of the today courses in the background by making
 * a request to the server and saving back the results.
 *
 * This is done so that the local data is up to date for the notifications to correctly
 * show the last lectures.
 *
 * @author Riccardo Busetti
 */
class UpdateLocalTodayTimetableUseCase(
    private val timetableRepository: TimetableRepository
) : UseCase {

    fun updateLocalTodayTimetable(
        department: String,
        degree: String,
        studyPlan: String,
        page: String
    ) {
        val websiteUrl = WebSiteUrl.Builder()
            .useDeviceLanguage()
            .withDepartment(department)
            .withDegree(degree)
            .withStudyPlan(studyPlan)
            .onlyToday()
            .atPage(page)
            .build()

        timetableRepository.updateLocalTimetable(AppSection.TODAY, websiteUrl)
    }
}