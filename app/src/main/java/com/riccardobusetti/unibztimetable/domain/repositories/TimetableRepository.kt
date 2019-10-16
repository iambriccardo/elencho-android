package com.riccardobusetti.unibztimetable.domain.repositories

import com.riccardobusetti.unibztimetable.data.network.WebSiteUrl
import com.riccardobusetti.unibztimetable.domain.strategies.RemoteTimetableStrategy

/**
 * Repository implementation which will get the timetable from different strategies depending
 * on many different conditions.
 *
 * @author Riccardo Busetti
 */
class TimetableRepository(
    private val remoteTimetableStrategy: RemoteTimetableStrategy
) : Repository {

    fun getTimetable(webSiteUrl: WebSiteUrl) = remoteTimetableStrategy.getTimetable(webSiteUrl)
}