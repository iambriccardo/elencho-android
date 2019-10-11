package com.riccardobusetti.unibztimetable.domain.usecases

import com.riccardobusetti.unibztimetable.domain.entities.Course
import com.riccardobusetti.unibztimetable.domain.entities.Day
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository
import com.riccardobusetti.unibztimetable.network.WebSiteLink
import com.riccardobusetti.unibztimetable.utils.DateUtils
import java.util.*

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
        val webSiteLink = WebSiteLink.Builder()
            .useDeviceLanguage()
            .withDepartment(department)
            .withDegree(degree)
            .withStudyPlan(studyPlan)
            .onlyToday()
            .atPage(page)
            .build()

        return timetableRepository.getTimetable(webSiteLink)
    }

    // TODO: test why the algorithm fails.
    fun getTodayTimetableWithOnGoingCourse(
        department: String,
        degree: String,
        academicYear: String,
        page: String
    ) =
        getTodayTimetable(department, degree, academicYear, page).map { day ->
            Day(day.date, day.courses.map { course ->
                val time = course.time.split("-")
                val startTime = time[0]
                val endTime = time[1]

                val courseStartDate =
                    "${day.date} ${DateUtils.getCurrentCalendar().get(Calendar.YEAR)} $startTime"
                val courseEndDate =
                    "${day.date} ${DateUtils.getCurrentCalendar().get(Calendar.YEAR)} $endTime"

                Course(
                    course.title,
                    course.location,
                    course.time,
                    course.professor,
                    course.type,
                    DateUtils.isCourseOnGoing(courseStartDate, courseEndDate)
                )
            })
        }
}