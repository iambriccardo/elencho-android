package com.riccardobusetti.unibztimetable.services

import android.app.IntentService
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.Day
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository
import com.riccardobusetti.unibztimetable.domain.strategies.LocalTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.RemoteTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.usecases.GetTodayTimetableUseCase
import com.riccardobusetti.unibztimetable.utils.NotificationUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ShowTodayTimetableIntentService : IntentService(ShowTodayTimetableIntentService::class.java.simpleName) {

    private val getTodayTimetableUseCase = GetTodayTimetableUseCase(
        this,
        TimetableRepository(
            LocalTimetableStrategy(this),
            RemoteTimetableStrategy()
        )
    )

    override fun onHandleIntent(p0: Intent?) {
        runBlocking {
            val localTimetable = withContext(Dispatchers.IO) {
                getTodayTimetableUseCase.getLocalTodayTimetable()
            }

            showNotification(localTimetable.first())
        }
    }

    private fun showNotification(day: Day) {
        val builder = NotificationCompat.Builder(this@ShowTodayTimetableIntentService, NotificationUtils.DAILY_UPDATES_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_today)
            .setContentTitle("You have ${day.courses.size} courses today.")
            .setContentText("The first course will be ${day.courses.first().title}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this@ShowTodayTimetableIntentService)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, builder.build())
        }
    }
}