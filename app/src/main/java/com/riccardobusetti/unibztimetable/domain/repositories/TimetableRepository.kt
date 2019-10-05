package com.riccardobusetti.unibztimetable.domain.repositories

import com.riccardobusetti.unibztimetable.domain.strategies.CachedTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.RemoteTimetableStrategy
import com.riccardobusetti.unibztimetable.network.WebSiteLink

class TimetableRepository(
    private val remoteTimetableStrategy: RemoteTimetableStrategy,
    private val cachedTimetableStrategy: CachedTimetableStrategy
) : Repository {

    // TODO: implement cache usage when needed
    fun getTimetable(webSiteLink: WebSiteLink) = remoteTimetableStrategy.getTimetable(webSiteLink)
}