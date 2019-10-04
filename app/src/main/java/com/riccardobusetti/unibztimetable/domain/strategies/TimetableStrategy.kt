package com.riccardobusetti.unibztimetable.domain.strategies

import com.riccardobusetti.unibztimetable.domain.entities.Day

/**
 * Interface describing the behavior that any strategy should implement if they
 * want to fetch the timetable.
 *
 * @author Riccardo Busetti
 */
interface TimetableStrategy : Strategy {

    /**
     * Gets the timetable of today.
     */
    fun getTodayTimetable(
        department: String,
        degree: String,
        academicYear: String,
        page: String
    ): List<Day>

    /**
     * Gets the timetable for the next 7 days of the week.
     */
    fun getNext7DaysTimetable(
        department: String,
        degree: String,
        academicYear: String,
        page: String
    ): List<Day>

    /**
     * Gets the timetable with all the parameters free of choice.
     */
    fun getTimetable(
        language: String,
        department: String,
        degree: String,
        academicYear: String,
        fromDate: String,
        toDate: String,
        page: String
    ): List<Day>
}