package com.riccardobusetti.unibztimetable.domain.entities

data class Course(
    val time: String,
    val title: String,
    val location: String,
    val professor: String,
    val type: String,
    val isOngoing: Boolean = false
) {

    companion object {
        private const val TIME_REGEX = "-"
    }

    fun getStartTime() = time.split(TIME_REGEX)[0]

    fun getEndTime() = time.split(TIME_REGEX)[1]
}

fun Course.toLecture(day: Day, appSection: AppSection) = Lecture(
    date = day.date,
    time = this.time,
    title = this.title,
    location = this.location,
    professor = this.professor,
    type = this.type,
    appSection = appSection
)