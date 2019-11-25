package com.riccardobusetti.unibztimetable.domain.entities

import com.riccardobusetti.unibztimetable.utils.DateUtils
import org.threeten.bp.LocalDateTime

data class Kourse(
    val id: Int? = null,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val room: String,
    val description: String,
    val professor: String,
    val type: String
) {

    fun isOngoing() = DateUtils.getCurrentLocalDateTime() in startDateTime..endDateTime

    fun isFinished() = DateUtils.getCurrentLocalDateTime() > endDateTime

    fun isDayPassed() = endDateTime < DateUtils.getCurrentLocalDateTime(true)
}