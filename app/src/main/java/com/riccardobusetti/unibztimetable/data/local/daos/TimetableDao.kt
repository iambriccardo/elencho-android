package com.riccardobusetti.unibztimetable.data.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.riccardobusetti.unibztimetable.domain.entities.AppSection
import com.riccardobusetti.unibztimetable.domain.entities.Lecture

@Dao
interface TimetableDao {

    @Query("SELECT * FROM timetable")
    fun getTimetable(): List<Lecture>

    @Query("SELECT * FROM timetable WHERE app_section = (:appSection)")
    fun getTimetable(appSection: AppSection): List<Lecture>

    @Insert
    fun insertLecture(lecture: Lecture)

    @Delete
    fun deleteLecture(lecture: Lecture)

    @Query("DELETE FROM timetable")
    fun deleteTimetable()

    @Query("DELETE FROM timetable WHERE app_section = (:appSection)")
    fun deleteTimetable(appSection: AppSection)
}