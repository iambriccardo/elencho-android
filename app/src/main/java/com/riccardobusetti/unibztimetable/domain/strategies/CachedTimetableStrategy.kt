package com.riccardobusetti.unibztimetable.domain.strategies

import com.riccardobusetti.unibztimetable.data.network.WebSiteUrl
import com.riccardobusetti.unibztimetable.domain.entities.Day

/**
 * Cached implementation for fetching the timetable.
 *
 * This implementation will be used to improve the loadingState speed
 * by leveraging on a key-value pair cache system.
 *
 * @author Riccardo Busetti
 */
class CachedTimetableStrategy : TimetableStrategy {

    /**
     * @inheritDoc
     */
    override fun getTimetable(webSiteUrl: WebSiteUrl): List<Day> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}