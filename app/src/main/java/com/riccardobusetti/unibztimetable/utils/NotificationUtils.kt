package com.riccardobusetti.unibztimetable.utils

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.riccardobusetti.unibztimetable.ui.main.MainActivity

object NotificationUtils {

    const val DAILY_UPDATES_CHANNEL_ID = "1"

    fun createNotificationChannel(context: Context, androidNotificationChannel: MainActivity.AndroidNotificationChannel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(IntentService.NOTIFICATION_SERVICE) as NotificationManager

            val channel = NotificationChannel(androidNotificationChannel.id, androidNotificationChannel.name, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = androidNotificationChannel.description

            notificationManager.createNotificationChannel(channel)
        }
    }
}