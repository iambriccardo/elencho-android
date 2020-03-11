package com.riccardobusetti.unibztimetable.data.local.converters

import androidx.room.TypeConverter
import com.riccardobusetti.unibztimetable.domain.entities.app.AppSection

class AppSectionConverter {

    @TypeConverter
    fun toAppSection(value: Int) = AppSection.values().first { it.type == value }

    @TypeConverter
    fun toInt(value: AppSection) = value.type
}