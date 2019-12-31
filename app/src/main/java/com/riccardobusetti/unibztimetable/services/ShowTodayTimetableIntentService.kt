package com.riccardobusetti.unibztimetable.services

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.Course
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository
import com.riccardobusetti.unibztimetable.domain.strategies.LocalTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.RemoteTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.usecases.GetTodayTimetableUseCase
import com.riccardobusetti.unibztimetable.ui.main.MainActivity
import com.riccardobusetti.unibztimetable.utils.DateUtils
import com.riccardobusetti.unibztimetable.utils.NotificationUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class ShowTodayTimetableIntentService : JobIntentService() {

    companion object {
        private const val TIME_PATTERN = "HH:mm"
        private const val JOB_ID = 1

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, ShowTodayTimetableIntentService::class.java, JOB_ID, intent)
        }
    }

    private val getTodayTimetableUseCase = GetTodayTimetableUseCase(
        TimetableRepository(
            LocalTimetableStrategy(this),
            RemoteTimetableStrategy()
        )
    )

    override fun onHandleWork(intent: Intent) {
        runBlocking {
            try {
                val localTimetable = withContext(Dispatchers.IO) {
                    getTodayTimetableUseCase.getLocalTodayTimetable()
                }

                if (localTimetable.isNotEmpty())
                    showCoursesNotification(localTimetable)
                else
                    showNoCoursesNotification()
            } catch (e: Exception) {
                showNotification("Notification error", "${e.message}", "${e.stackTrace}")
            }
        }
    }

    private fun showCoursesNotification(courses: List<Course>) {
        showNotification(
            getNotificationTitle(courses),
            getNotificationText(courses),
            getNotificationBigText(courses)
        )
    }

    private fun showNoCoursesNotification() {
        showNotification(
            getString(R.string.daily_notification_no_courses_title),
            getString(R.string.daily_notification_no_courses_text)
        )
    }

    private fun getNotificationTitle(courses: List<Course>) =
        getString(R.string.daily_notification_courses_title, courses.size)

    private fun getNotificationText(courses: List<Course>) = getString(
        R.string.daily_notification_courses_text,
        DateUtils.formatLocalDateTime(courses.first().startDateTime, TIME_PATTERN)
    )

    private fun getNotificationBigText(courses: List<Course>): String {
        return courses.joinToString("\n") {
            getString(
                R.string.daily_notification_courses_big_text,
                DateUtils.formatLocalDateTime(it.startDateTime, TIME_PATTERN),
                it.description
            )
        }
    }

    private fun showNotification(title: String, text: String, bigText: String? = null) {
        val resultIntent = Intent(this, MainActivity::class.java)

        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val builder = NotificationCompat.Builder(
            this@ShowTodayTimetableIntentService,
            NotificationUtils.DAILY_UPDATES_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_elencho)
            .setContentTitle(title)
            .setContentText(text)
            .setColor(
                ContextCompat.getColor(
                    this@ShowTodayTimetableIntentService,
                    R.color.colorPrimary
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)

        if (bigText != null) builder.setStyle(
            NotificationCompat.BigTextStyle()
                .bigText(bigText)
        )

        with(NotificationManagerCompat.from(this@ShowTodayTimetableIntentService)) {
            notify(1, builder.build())
        }
    }
}