package com.riccardobusetti.unibztimetable.domain.strategies

import com.riccardobusetti.unibztimetable.domain.entities.Day

/**
 * Cached implementation for fetching the timetable.
 *
 * This implementation will be used to improve the loading speed
 * by leveraging on a key-value pair cache system.
 *
 * @author Riccardo Busetti
 */
class CachedTimetableStrategy : TimetableStrategy {

    override fun getTodayTimetable(
        department: String,
        degree: String,
        academicYear: String,
        page: String
    ): List<Day> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getNext7DaysTimetable(
        department: String,
        degree: String,
        academicYear: String,
        page: String
    ): List<Day> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTimetable(
        language: String,
        department: String,
        degree: String,
        academicYear: String,
        fromDate: String,
        toDate: String,
        page: String
    ): List<Day> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}