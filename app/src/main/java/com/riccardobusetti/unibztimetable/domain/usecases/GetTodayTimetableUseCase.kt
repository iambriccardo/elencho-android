package com.riccardobusetti.unibztimetable.domain.usecases

import android.content.Context
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.data.remote.WebSiteUrl
import com.riccardobusetti.unibztimetable.domain.entities.AppSection
import com.riccardobusetti.unibztimetable.domain.entities.Course
import com.riccardobusetti.unibztimetable.domain.entities.Day
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository
import com.riccardobusetti.unibztimetable.utils.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Use case which will manage the today timetable that is responsible of
 * showing to the user the timetable of the current day.
 *
 * @author Riccardo Busetti
 */
class GetTodayTimetableUseCase(
    private val context: Context,
    private val timetableRepository: TimetableRepository
) : UseCase {

    /**
     * Gets the timetable of today.
     */
    fun getTodayTimetable(
        department: String,
        degree: String,
        studyPlan: String,
        page: String,
        isInternetAvailable: Boolean
    ): Flow<List<Day>> {
        val websiteUrl = WebSiteUrl.Builder()
            .useDeviceLanguage()
            .withDepartment(department)
            .withDegree(degree)
            .withStudyPlan(studyPlan)
            .onlyToday()
            .atPage(page)
            .build()

        return timetableRepository.getTimetable(AppSection.TODAY, websiteUrl, isInternetAvailable)
    }

    /**
     * Gets the today timetable and formats it to show ongoing courses.
     */
    fun getTodayTimetableWithOnGoingCourse(
        department: String,
        degree: String,
        academicYear: String,
        page: String,
        isInternetAvailable: Boolean
    ) =
        getTodayTimetable(
            department,
            degree,
            academicYear,
            page,
            isInternetAvailable
        ).map { newTimetable ->
            newTimetable.flatMap { day ->
                day.courses.filter {
                    !DateUtils.isCourseFinished(
                        DateUtils.mergeDayAndCourseTimeData(
                            day.date,
                            it.getEndTime()
                        )
                    )
                }.map {
                    Course(
                        it.time,
                        it.title,
                        it.location,
                        it.professor,
                        it.type,
                        DateUtils.isCourseOnGoing(
                            DateUtils.mergeDayAndCourseTimeData(day.date, it.getStartTime()),
                            DateUtils.mergeDayAndCourseTimeData(day.date, it.getEndTime())
                        )
                    )
                }.groupBy {
                    it.isOngoing
                }.map {
                    if (it.key) {
                        Day(day.date, it.value, context.getString(R.string.now), true)
                    } else {
                        Day(
                            day.date,
                            it.value,
                            context.getString(R.string.upcoming_lectures),
                            false
                        )
                    }
                }
            }.filter { it.courses.isNotEmpty() }
        }

    /**
     * Gets the local timetable for today.
     */
    fun getLocalTodayTimetable() = timetableRepository.getLocalTimetable(AppSection.TODAY)
}