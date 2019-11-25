package com.riccardobusetti.unibztimetable.data.local.converters

import androidx.room.TypeConverter
import com.riccardobusetti.unibztimetable.utils.DateUtils
import org.threeten.bp.LocalDateTime

class LocalDateTimeConverter {

    @TypeConverter
    fun toLocalDateTime(value: String) = DateUtils.parseCourseDateTime(value)

    @TypeConverter
    fun toString(value: LocalDateTime) = DateUtils.formatCourseDateTime(value)
}