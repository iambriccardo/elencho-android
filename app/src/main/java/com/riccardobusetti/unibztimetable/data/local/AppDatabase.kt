package com.riccardobusetti.unibztimetable.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.riccardobusetti.unibztimetable.data.local.daos.TimetableDao
import com.riccardobusetti.unibztimetable.domain.entities.AppSectionConverter
import com.riccardobusetti.unibztimetable.domain.entities.Lecture

@Database(entities = [Lecture::class], version = 1)
@TypeConverters(AppSectionConverter::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "unibz_timetable"
    }

    abstract fun timetableDao(): TimetableDao
}