package com.riccardobusetti.unibztimetable.data.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.riccardobusetti.unibztimetable.domain.entities.Course
import com.riccardobusetti.unibztimetable.domain.entities.app.AppSection

@Dao
interface TimetableDao {

    @Query("SELECT * FROM timetable")
    fun getTimetable(): List<Course>

    @Query("SELECT * FROM timetable WHERE app_section = (:appSection)")
    fun getTimetable(appSection: AppSection): List<Course>

    @Insert
    fun insertCourse(course: Course)

    @Delete
    fun deleteCourse(course: Course)

    @Query("DELETE FROM timetable")
    fun deleteTimetable()

    @Query("DELETE FROM timetable WHERE app_section = (:appSection)")
    fun deleteTimetable(appSection: AppSection)
}