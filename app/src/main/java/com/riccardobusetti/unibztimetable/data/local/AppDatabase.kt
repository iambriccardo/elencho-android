package com.riccardobusetti.unibztimetable.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.riccardobusetti.unibztimetable.data.local.converters.AppSectionConverter
import com.riccardobusetti.unibztimetable.data.local.converters.LocalDateTimeConverter
import com.riccardobusetti.unibztimetable.data.local.daos.TimetableDao
import com.riccardobusetti.unibztimetable.domain.entities.Course

@Database(entities = [Course::class], version = 2)
@TypeConverters(AppSectionConverter::class, LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "unibz_timetable"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE IF EXISTS timetable")
            }
        }
    }

    abstract fun timetableDao(): TimetableDao
}