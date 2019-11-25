package com.riccardobusetti.unibztimetable.data.local.converters

import androidx.room.TypeConverter
import com.riccardobusetti.unibztimetable.utils.DateUtils
import org.threeten.bp.LocalDateTime

class LocalDateTimeConverter {

    @TypeConverter
    fun toLocalDateTime(value: String) = DateUtils.convertCourseDateTime(value)

    @TypeConverter
    fun toString(value: LocalDateTime) = value.toString()
}