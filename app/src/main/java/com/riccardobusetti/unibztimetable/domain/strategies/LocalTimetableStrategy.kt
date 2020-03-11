package com.riccardobusetti.unibztimetable.domain.strategies

import android.content.Context
import androidx.room.Room
import com.riccardobusetti.unibztimetable.data.local.AppDatabase
import com.riccardobusetti.unibztimetable.domain.entities.Course
import com.riccardobusetti.unibztimetable.domain.entities.app.AppSection

/**
 * Local implementation for fetching the timetable.
 *
 * This implementation will leverage on a DB that will load data also offline.
 *
 * @author Riccardo Busetti
 */
class LocalTimetableStrategy(private val context: Context) : TimetableStrategy<AppSection?> {

    companion object {

        fun getAppDatabase(context: Context) = lazy {
            Room.databaseBuilder(
                context,
                AppDatabase::class.java, AppDatabase.DATABASE_NAME
            ).addMigrations(AppDatabase.MIGRATION_1_2).build()
        }.value
    }

    private val timetableDao get() = getAppDatabase(context).timetableDao()

    /**
     * @inheritDoc
     */
    override fun getTimetable(query: AppSection?): List<Course> {
        return if (query != null) {
            timetableDao.getTimetable(query)
        } else {
            timetableDao.getTimetable()
        }
    }

    fun insertTimetable(courses: List<Course>) {
        courses.forEach {
            timetableDao.insertCourse(it)
        }
    }

    fun deleteTimetable() {
        timetableDao.deleteTimetable()
    }

    fun deleteTimetable(appSection: AppSection) {
        timetableDao.deleteTimetable(appSection)
    }

    fun deleteTodayTimetable() {
        timetableDao.deleteTimetable(AppSection.TODAY)
    }
}