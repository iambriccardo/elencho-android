package com.riccardobusetti.unibztimetable.services

import android.app.IntentService
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.Course
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository
import com.riccardobusetti.unibztimetable.domain.strategies.LocalTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.RemoteTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.usecases.GetTodayTimetableUseCase
import com.riccardobusetti.unibztimetable.utils.DateUtils
import com.riccardobusetti.unibztimetable.utils.NotificationUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ShowTodayTimetableIntentService : IntentService(ShowTodayTimetableIntentService::class.java.simpleName) {

    companion object {
        private const val TIME_PATTERN = "HH:mm"
    }

    private val getTodayTimetableUseCase = GetTodayTimetableUseCase(
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

            showNotification(localTimetable)
        }
    }

    private fun showNotification(courses: List<Course>) {
        val builder = NotificationCompat.Builder(
            this@ShowTodayTimetableIntentService,
            NotificationUtils.DAILY_UPDATES_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_elencho)
            .setContentTitle(getNotificationTitle(courses))
            .setContentText(getNotificationText(courses))
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(getNotificationBigText(courses))
            )
            .setColor(
                ContextCompat.getColor(
                    this@ShowTodayTimetableIntentService,
                    R.color.colorPrimary
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this@ShowTodayTimetableIntentService)) {
            notify(1, builder.build())
        }
    }

    private fun getNotificationTitle(courses: List<Course>) =
        getString(R.string.daily_notification_title, courses.size)

    private fun getNotificationText(courses: List<Course>) = getString(
        R.string.daily_notification_text,
        DateUtils.formatLocalDateTime(courses.first().startDateTime, TIME_PATTERN)
    )

    private fun getNotificationBigText(courses: List<Course>): String {
        return courses.joinToString("\n") {
            getString(
                R.string.daily_notification_big_text,
                DateUtils.formatLocalDateTime(it.startDateTime, TIME_PATTERN),
                it.description
            )
        }
    }
}