package com.riccardobusetti.unibztimetable.domain.repositories

import android.util.Log
import com.riccardobusetti.unibztimetable.data.remote.WebSiteUrl
import com.riccardobusetti.unibztimetable.domain.entities.AppSection
import com.riccardobusetti.unibztimetable.domain.entities.Course
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

    companion object {
        private const val TAG = "TimetableRepository"
    }

    fun getTimetable(
        appSection: AppSection,
        webSiteUrl: WebSiteUrl
    ) = flow {
        // TODO: handle DB related error.
        val localTimetable = getLocalTimetable(appSection)

        // Checking if the timetable saved on the database is of the same day.
        val showLocalData = localTimetable.isNotEmpty() && !isLocalTodayTimetableOld(localTimetable)

        if (appSection == AppSection.TODAY && showLocalData) {
            // We emit the local timetable first, so the user doesn't have to wait for the remote
            // data to be loaded.
            Log.d(TAG, "Emittig timetable from the database: $localTimetable")
            emit(localTimetable)
            Log.d(TAG, "Emitted timetable from the database")
        }

        // TODO: handle remote error.
        val remoteTimetable = getRemoteTimetable(webSiteUrl)
        Log.d(TAG, "Emittig timetable from remote: $remoteTimetable")
        emit(remoteTimetable)
        Log.d(TAG, "Emitted timetable from remote")

        // For now we support only the TODAY section in the database.
        if (appSection == AppSection.TODAY) {
            localTimetableStrategy.deleteTodayTimetable()
            localTimetableStrategy.insertTimetable(remoteTimetable.map {
                it.appendAppSection(appSection)
            })
        }
    }

    fun getLocalTimetable(appSection: AppSection) = localTimetableStrategy.getTimetable(appSection)

    fun updateLocalTimetable(
        appSection: AppSection,
        webSiteUrl: WebSiteUrl
    ) {
        val remoteTimetable = remoteTimetableStrategy.getTimetable(webSiteUrl)

        localTimetableStrategy.deleteTodayTimetable()
        localTimetableStrategy.insertTimetable(remoteTimetable.map {
            it.appendAppSection(appSection)
        })

        Log.d(TAG, "Updating timetable")
    }

    fun deleteLocalTimetable() {
        localTimetableStrategy.deleteTimetable()
    }

    private fun getRemoteTimetable(webSiteUrl: WebSiteUrl) =
        remoteTimetableStrategy.getTimetable(webSiteUrl)

    private fun isLocalTodayTimetableOld(localTimetable: List<Course>) =
        localTimetable.first().isDayPassed()
}