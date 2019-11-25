package com.riccardobusetti.unibztimetable.domain.entities

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

        // TODO: format start and end time.
        fun build(course: Course) = DisplayableCourse(
            startTime = "${course.startDateTime.hour}:${course.startDateTime.minute}",
            endTime = "${course.endDateTime.hour}:${course.endDateTime.minute}",
            room = course.room,
            description = course.description,
            professor = course.professor,
            type = course.type,
            isOngoing = course.isOngoing()
        )
    }
}