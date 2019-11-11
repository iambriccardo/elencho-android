package com.riccardobusetti.unibztimetable.domain.strategies

import android.content.Context
import androidx.room.Room
import com.riccardobusetti.unibztimetable.data.local.AppDatabase
import com.riccardobusetti.unibztimetable.domain.entities.AppSection
import com.riccardobusetti.unibztimetable.domain.entities.Day
import com.riccardobusetti.unibztimetable.domain.entities.toLecture
import com.riccardobusetti.unibztimetable.domain.entities.toTimetable

/**
 * Local implementation for fetching the timetable.
 *
 * This implementation will leverage on a DB that will load data also offline.
 *
 * @author Riccardo Busetti
 */
class LocalTimetableStrategy(private val context: Context) {

    companion object {

        fun getAppDatabase(context: Context) = lazy {
            Room.databaseBuilder(
                context,
                AppDatabase::class.java, AppDatabase.DATABASE_NAME
            ).build()
        }.value
    }

    private val timetableDao get() = getAppDatabase(context).timetableDao()

    fun getTimetable() = timetableDao.getTimetable().toTimetable()

    fun getTimetable(appSection: AppSection) = timetableDao.getTimetable(appSection).toTimetable()

    fun insertTimetable(appSection: AppSection, timetable: List<Day>) {
        timetable.forEach { day ->
            day.courses.forEach { course ->
                timetableDao.insertLecture(course.toLecture(day, appSection))
            }
        }
    }

    fun deleteTimetable(appSection: AppSection) {
        timetableDao.deleteTimetable(appSection)
    }
}