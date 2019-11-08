package com.riccardobusetti.unibztimetable.domain.repositories

import com.riccardobusetti.unibztimetable.data.remote.WebSiteUrl
import com.riccardobusetti.unibztimetable.domain.entities.AppSection
import com.riccardobusetti.unibztimetable.domain.strategies.LocalTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.RemoteTimetableStrategy
import kotlinx.coroutines.flow.flow

/**
 * Repository implementation which will get the timetable from different strategies depending
 * on many different conditions.
 *
 * @author Riccardo Busetti
 */
class TimetableRepository(
    private val localTimetableStrategy: LocalTimetableStrategy,
    private val remoteTimetableStrategy: RemoteTimetableStrategy
) : Repository {

    fun getTimetable(
        appSection: AppSection,
        webSiteUrl: WebSiteUrl
    ) = flow {
        var localTimetable = localTimetableStrategy.getTimetable(appSection)

        // We emit the local timetable first, so the user doesn't have to wait for the remote
        // data to be loaded.
        if (localTimetable.isNotEmpty()) emit(localTimetable)

        val remoteTimetable = remoteTimetableStrategy.getTimetable(webSiteUrl)
        localTimetableStrategy.deleteTimetable(appSection)
        localTimetableStrategy.insertTimetable(appSection, remoteTimetable)

        localTimetable = localTimetableStrategy.getTimetable(appSection)
        emit(localTimetable)
    }
}