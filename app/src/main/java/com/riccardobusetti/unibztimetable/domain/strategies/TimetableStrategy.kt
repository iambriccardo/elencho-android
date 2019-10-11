package com.riccardobusetti.unibztimetable.domain.strategies

import com.riccardobusetti.unibztimetable.data.network.WebSiteLink
import com.riccardobusetti.unibztimetable.domain.entities.Day

/**
 * Interface describing the behavior that any strategy should implement if they
 * want to fetch the timetable.
 *
 * @author Riccardo Busetti
 */
interface TimetableStrategy : Strategy {

    /**
     * Gets the timetable from a specific [WebSiteLink].
     */
    fun getTimetable(webSiteLink: WebSiteLink): List<Day>
}