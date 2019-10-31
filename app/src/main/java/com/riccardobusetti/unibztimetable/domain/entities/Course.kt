package com.riccardobusetti.unibztimetable.domain.entities

class Course(
    val title: String,
    val location: String,
    val time: String,
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