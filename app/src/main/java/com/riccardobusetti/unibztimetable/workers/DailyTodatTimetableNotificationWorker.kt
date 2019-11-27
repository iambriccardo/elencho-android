package com.riccardobusetti.unibztimetable.workers

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.Course
import com.riccardobusetti.unibztimetable.domain.entities.UserPrefs
import com.riccardobusetti.unibztimetable.domain.entities.safeGet
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository
import com.riccardobusetti.unibztimetable.domain.repositories.UserPrefsRepository
import com.riccardobusetti.unibztimetable.domain.strategies.LocalTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.RemoteTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.SharedPreferencesUserPrefsStrategy
import com.riccardobusetti.unibztimetable.domain.usecases.GetTodayTimetableUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.GetUserPrefsUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.UpdateLocalTodayTimetableUseCase
import com.riccardobusetti.unibztimetable.utils.NotificationUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class DailyTodatTimetableNotificationWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    private val timetableRepository = TimetableRepository(
        LocalTimetableStrategy(context),
        RemoteTimetableStrategy()
    )

    private val getUserPrefsUseCase = GetUserPrefsUseCase(
        UserPrefsRepository(
            SharedPreferencesUserPrefsStrategy(context)
        )
    )

    private val updateLocalTodayTimetableUseCase = UpdateLocalTodayTimetableUseCase(
        timetableRepository
    )

    private val getTodayTimetableUseCase = GetTodayTimetableUseCase(
        timetableRepository
    )

    override suspend fun doWork(): Result = coroutineScope {
        withContext(Dispatchers.IO) {
            // TODO: handle errors, internet connectivity absence and edge cases.
            updateTodayTimetable(getUserPrefs())
        }

        val localTimetable = withContext(Dispatchers.IO) {
            getTodayTimetableUseCase.getLocalTodayTimetable()
        }

        showNotification(localTimetable)

        Result.success()
    }

    private suspend fun getUserPrefs() = withContext(Dispatchers.IO) {
        getUserPrefsUseCase.getUserPrefs()
    }

    private fun updateTodayTimetable(
        userPrefs: UserPrefs
    ) = updateLocalTodayTimetableUseCase.updateLocalTodayTimetable(
        userPrefs.prefs.safeGet(UserPrefs.Pref.DEPARTMENT_ID),
        userPrefs.prefs.safeGet(UserPrefs.Pref.DEGREE_ID),
        userPrefs.prefs.safeGet(UserPrefs.Pref.STUDY_PLAN_ID),
        "0"
    )

    private fun showNotification(courses: List<Course>) {
        val builder =
            NotificationCompat.Builder(context, NotificationUtils.DAILY_UPDATES_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_today)
                .setContentTitle("You have ${courses.size} courses today.")
                .setContentText("The first course will be ${courses.first().description}")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, builder.build())
        }
    }
}