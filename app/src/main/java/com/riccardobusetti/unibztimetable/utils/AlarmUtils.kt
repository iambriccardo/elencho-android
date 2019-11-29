package com.riccardobusetti.unibztimetable.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import java.util.*

object AlarmUtils {

    fun <T> scheduleSingleAlarm(context: Context, clazz: Class<T>, calendar: Calendar) {
        val intent = Intent(context, clazz)

        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    fun <T> scheduleRepeatingAlarm(
        context: Context,
        clazz: Class<T>,
        calendar: Calendar,
        frequency: Long
    ) {
        val intent = Intent(context, clazz)

        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            frequency,
            pendingIntent
        )
    }

    fun <T> cancelAlarm(context: Context, clazz: Class<T>) {
        val intent = Intent(context, clazz)

        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }
}