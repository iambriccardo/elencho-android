package com.riccardobusetti.unibztimetable.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.riccardobusetti.unibztimetable.utils.DateUtils
import org.threeten.bp.LocalDateTime

@Entity(tableName = "timetable")
data class Course(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "startDateTime") val startDateTime: LocalDateTime,
    @ColumnInfo(name = "endDateTime") val endDateTime: LocalDateTime,
    @ColumnInfo(name = "room") val room: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "professor") val professor: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "app_section") val appSection: AppSection? = null
) {

    fun isOngoing() = DateUtils.getCurrentLocalDateTime() in startDateTime..endDateTime

    fun isFinished() = DateUtils.getCurrentLocalDateTime() > endDateTime

    fun isDayPassed() = endDateTime < DateUtils.getCurrentLocalDateTime(true)
}