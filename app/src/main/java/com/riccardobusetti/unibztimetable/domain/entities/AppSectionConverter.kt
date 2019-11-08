package com.riccardobusetti.unibztimetable.domain.entities

import androidx.room.TypeConverter

class AppSectionConverter {

    @TypeConverter
    fun toAppSection(value: Int) = AppSection.values().first { it.type == value }

    @TypeConverter
    fun toInt(value: AppSection) = value.type
}