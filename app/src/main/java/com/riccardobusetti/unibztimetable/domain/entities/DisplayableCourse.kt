package com.riccardobusetti.unibztimetable.domain.entities

import com.riccardobusetti.unibztimetable.utils.DateUtils

data class DisplayableCourse(
    val startTime: String,
    val endTime: String,
    val room: String,
    val description: String,
    val professor: String,
    val type: String,
    val isOngoing: Boolean = false
) {


    companion object {

        private const val TIME_PATTERN = "HH:mm"

        fun build(course: Course) = DisplayableCourse(
            startTime = DateUtils.formatLocalDateTime(course.startDateTime, TIME_PATTERN),
            endTime = DateUtils.formatLocalDateTime(course.endDateTime, TIME_PATTERN),
            room = course.room,
            description = course.description,
            professor = course.professor,
            type = course.type,
            isOngoing = course.isOngoing()
        )
    }
}