package com.riccardobusetti.unibztimetable.domain.strategies

import android.content.Context
import androidx.room.Room
import com.riccardobusetti.unibztimetable.data.local.AppDatabase
import com.riccardobusetti.unibztimetable.domain.entities.AppSection
import com.riccardobusetti.unibztimetable.domain.entities.Course

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

    fun getTimetable() = timetableDao.getTimetable()

    fun getTimetable(appSection: AppSection) = timetableDao.getTimetable(appSection)

    fun insertTimetable(courses: List<Course>) {
        courses.forEach {
            timetableDao.insertCourse(it)
        }
    }

    fun deleteTodayTimetable() {
        timetableDao.deleteTimetable(AppSection.TODAY)
    }
}