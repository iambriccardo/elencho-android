package com.riccardobusetti.unibztimetable.domain.strategies

import com.riccardobusetti.unibztimetable.domain.entities.Course

/**
 * Interface describing the behavior that any strategy should implement if they
 * want to fetch the timetable.
 *
 * @param Query is a generic that represents the query for the timetable.
 *        The query can be a url, a filter for the database or anything else that can
 *        filter the given data.
 *
 * @author Riccardo Busetti
 */
interface TimetableStrategy<Query> : Strategy {

    /**
     * Gets the timetable from a specific source with a defined query which can
     * also be null if not needed.
     */
    fun getTimetable(query: Query): List<Course>
}