package com.riccardobusetti.unibztimetable.domain.repositories

import com.riccardobusetti.unibztimetable.domain.strategies.CachedTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.RemoteTimetableStrategy

class TimetableRepository(
    private val remoteTimetableStrategy: RemoteTimetableStrategy,
    private val cachedTimetableStrategy: CachedTimetableStrategy
) : Repository {


}