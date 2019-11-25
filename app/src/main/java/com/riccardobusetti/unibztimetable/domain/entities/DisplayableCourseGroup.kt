package com.riccardobusetti.unibztimetable.domain.entities

import com.riccardobusetti.unibztimetable.utils.DateUtils

data class DisplayableCourseGroup(
    val title: String,
    val isNow: Boolean = false,
    val courses: List<DisplayableCourse>
) {


    companion object {

        fun build(courses: List<Course>, customGrouping: ((Course) -> String?)? = null) =
            courses.groupBy {
                if (customGrouping != null) {
                    customGrouping(it) ?: defaultGrouping(it)
                } else {
                    defaultGrouping(it)
                }
            }.map {
                DisplayableCourseGroup(
                    title = it.key,
                    isNow = it.value.first().isOngoing(),
                    courses = it.value.map { course -> DisplayableCourse.build(course) }
                )
            }

        private fun defaultGrouping(course: Course) =
            DateUtils.formatLocalDateTime(course.startDateTime, "EEEE, dd MMM")
    }
}