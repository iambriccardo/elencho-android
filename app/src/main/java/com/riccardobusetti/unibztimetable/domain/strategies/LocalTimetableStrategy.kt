package com.riccardobusetti.unibztimetable.domain.strategies

import com.riccardobusetti.unibztimetable.data.network.WebSiteUrl
import com.riccardobusetti.unibztimetable.domain.entities.Day

/**
 * Local implementation for fetching the timetable.
 *
 * This implementation will leverage on a DB that will load data also offline.
 *
 * @author Riccardo Busetti
 */
class LocalTimetableStrategy : TimetableStrategy {

    /**
     * @inheritDoc
     */
    override fun getTimetable(webSiteUrl: WebSiteUrl): List<Day> {
        return emptyList()
    }
}