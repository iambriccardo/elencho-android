package com.riccardobusetti.unibztimetable.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity that represents a lecture.
 *
 * This entity is only used inside of the database because a [Lecture] contains the date from
 * [Day] and the details of the course from [Course]. This is done to create a more efficient
 * database schema.
 *
 * When querying data, then the [LocalTimetableStrategy] class will be responsible of entity mapping
 * between [Lecture] and [List] of [Day].
 */
@Entity(tableName = "timetable")
data class Lecture(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "time") val time: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "location") val location: String,
    @ColumnInfo(name = "professor") val professor: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "app_section") val appSection: AppSection
)

fun List<Lecture>.toTimetable() = this.groupBy {
    it.date
}.map {
    Day(it.key, it.value.map { lecture ->
        Course(
            time = lecture.time,
            title = lecture.title,
            location = lecture.location,
            professor = lecture.professor,
            type = lecture.type
        )
    })
}