package com.riccardobusetti.unibztimetable.domain.usecases

import com.riccardobusetti.unibztimetable.data.network.WebSiteUrl
import com.riccardobusetti.unibztimetable.domain.entities.Course
import com.riccardobusetti.unibztimetable.domain.entities.Day
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository
import com.riccardobusetti.unibztimetable.utils.DateUtils

/**
 * Use case which will manage the today timetable that is responsible of
 * showing to the user the timetable of the current day.
 *
 * @author Riccardo Busetti
 */
class GetTodayTimetableUseCase(
    private val timetableRepository: TimetableRepository
) : UseCase {

    /**
     * Gets the timetable of today.
     */
    fun getTodayTimetable(
        department: String,
        degree: String,
        studyPlan: String,
        page: String
    ): List<Day> {
        val websiteUrl = WebSiteUrl.Builder()
            .useDeviceLanguage()
            .withDepartment(department)
            .withDegree(degree)
            .withStudyPlan(studyPlan)
            .onlyToday()
            .atPage(page)
            .build()

        return timetableRepository.getTimetable(websiteUrl)
    }

    // TODO: test why the algorithm fails with italian dates.
    fun getTodayTimetableWithOnGoingCourse(
        department: String,
        degree: String,
        academicYear: String,
        page: String
    ) =
        getTodayTimetable(department, degree, academicYear, page).map { day ->
            Day(day.date, day.courses.map {
                Course(
                    it.title,
                    it.location,
                    it.time,
                    it.professor,
                    it.type,
                    DateUtils.isCourseOnGoing(
                        DateUtils.mergeDayAndCourseTimeData(day.date, it.getStartTime()),
                        DateUtils.mergeDayAndCourseTimeData(day.date, it.getEndTime())
                    )
                )
            }.filter {
                !DateUtils.isCourseFinished(
                    DateUtils.mergeDayAndCourseTimeData(
                        day.date,
                        it.getEndTime()
                    )
                )
            })
        }.filter { it.courses.isNotEmpty() }
}