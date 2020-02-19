package com.riccardobusetti.unibztimetable.receivers

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.riccardobusetti.unibztimetable.domain.entities.UserPrefs
import com.riccardobusetti.unibztimetable.domain.repositories.UserPrefsRepository
import com.riccardobusetti.unibztimetable.domain.strategies.SharedPreferencesUserPrefsStrategy
import com.riccardobusetti.unibztimetable.domain.usecases.GetUserPrefsUseCase
import com.riccardobusetti.unibztimetable.services.ShowTodayTimetableIntentService
import com.riccardobusetti.unibztimetable.utils.AlarmUtils
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            if (intent.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
                val dailyNotificationTime = GetUserPrefsUseCase(
                    UserPrefsRepository(
                        SharedPreferencesUserPrefsStrategy(context)
                    )
                ).execute(null).prefs[UserPrefs.Pref.DAILY_NOTIFICATION_TIME]

                dailyNotificationTime?.let {
                    val hourOfTheDay = it.split(":")[0]
                    val minute = it.split(":")[1]

                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hourOfTheDay))
                    calendar.set(Calendar.MINUTE, Integer.valueOf(minute))

                    AlarmUtils.cancelAlarm(context, AlarmUtils::class.java)
                    AlarmUtils.scheduleRepeatingAlarm(
                        context,
                        AlarmReceiver::class.java,
                        calendar,
                        AlarmManager.INTERVAL_DAY
                    )
                }
            } else {
                Intent(context, ShowTodayTimetableIntentService::class.java).apply {
                    ShowTodayTimetableIntentService.enqueueWork(context, this)
                }
            }
        }
    }
}