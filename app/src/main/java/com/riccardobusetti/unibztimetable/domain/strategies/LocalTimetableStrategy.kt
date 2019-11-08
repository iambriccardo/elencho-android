package com.riccardobusetti.unibztimetable.domain.strategies

import android.content.Context
import androidx.room.Room
import com.riccardobusetti.unibztimetable.data.local.AppDatabase
import com.riccardobusetti.unibztimetable.domain.entities.AppSection
import com.riccardobusetti.unibztimetable.domain.entities.Day
import com.riccardobusetti.unibztimetable.domain.entities.Lecture
import com.riccardobusetti.unibztimetable.domain.entities.toTimetable

/**
 * Local implementation for fetching the timetable.
 *
 * This implementation will leverage on a DB that will load data also offline.
 *
 * @author Riccardo Busetti
 */
class LocalTimetableStrategy(private val context: Context) {

    // TODO: find a way to use the singleton pattern here.
    private fun getAppDatabase() = lazy {
        Room.databaseBuilder(
            context,
            AppDatabase::class.java, AppDatabase.DATABASE_NAME
        ).build()
    }.value

    fun getTimetable() = getAppDatabase().timetableDao().getTimetable().toTimetable()

    fun getTimetable(appSection: AppSection) =
        getAppDatabase().timetableDao().getTimetable(appSection).toTimetable()

    fun insertTimetable(appSection: AppSection, timetable: List<Day>) {
        timetable.forEach { day ->
            day.courses.forEach { course ->
                getAppDatabase().timetableDao().insertLecture(
                    Lecture(
                        date = day.date,
                        time = course.time,
                        title = course.title,
                        location = course.location,
                        professor = course.professor,
                        type = course.type,
                        appSection = appSection
                    )
                )
            }
        }
    }

    fun deleteTimetable(appSection: AppSection) {
        getAppDatabase().timetableDao().deleteTimetable(appSection)
    }
}