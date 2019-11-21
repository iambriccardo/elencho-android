package com.riccardobusetti.unibztimetable.domain.repositories

import android.util.Log
import com.riccardobusetti.unibztimetable.data.remote.WebSiteUrl
import com.riccardobusetti.unibztimetable.domain.entities.AppSection
import com.riccardobusetti.unibztimetable.domain.entities.Day
import com.riccardobusetti.unibztimetable.domain.strategies.LocalTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.RemoteTimetableStrategy
import com.riccardobusetti.unibztimetable.utils.DateUtils
import com.riccardobusetti.unibztimetable.utils.exceptions.InternetNotAvailableException
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
        webSiteUrl: WebSiteUrl,
        isInternetAvailable: Boolean = true
    ) = flow {
        val localTimetable = localTimetableStrategy.getTimetable(appSection)
        Log.d(TAG, "Data queried from the database -> $localTimetable")

        // Checking if the timetable saved on the database is of the same day.
        val showLocalData = localTimetable.isNotEmpty() && !isLocalTodayTimetableOld(localTimetable)

        if (showLocalData) {
            // We emit the local timetable first, so the user doesn't have to wait for the remote
            // data to be loaded.
            Log.d(TAG, "Emittig timetable from the database.")
            emit(localTimetable)
        }

        if (isInternetAvailable) {
            val remoteTimetable = remoteTimetableStrategy.getTimetable(webSiteUrl)

            Log.d(TAG, "Emittig timetable from remote.")
            emit(remoteTimetable)

            // For now we support only the TODAY section in the database.
            if (appSection == AppSection.TODAY) {
                localTimetableStrategy.deleteTimetable(appSection)
                localTimetableStrategy.insertTimetable(appSection, remoteTimetable)
            }
        } else if (!isInternetAvailable && !showLocalData) {
            throw InternetNotAvailableException()
        }
    }

    fun updateLocalTimetable(
        appSection: AppSection,
        webSiteUrl: WebSiteUrl
    ) {
        val remoteTimetable = remoteTimetableStrategy.getTimetable(webSiteUrl)
        localTimetableStrategy.deleteTimetable(appSection)
        localTimetableStrategy.insertTimetable(appSection, remoteTimetable)
    }

    // TODO: now in the DB the date is memorized without the year, this leads to some issues.
    private fun isLocalTodayTimetableOld(localTimetable: List<Day>) =
        DateUtils.isDayPassed(
            DateUtils.mergeDayAndCourseTimeData(
                localTimetable.first().date,
                "00:00"
            )
        )
}