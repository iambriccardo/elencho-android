package com.riccardobusetti.unibztimetable.ui.main

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.services.UpdateTodayTimetableIntentService
import com.riccardobusetti.unibztimetable.ui.adapters.FragmentsAdapter
import com.riccardobusetti.unibztimetable.ui.canteen.CanteenFragment
import com.riccardobusetti.unibztimetable.ui.next7days.Next7DaysFragment
import com.riccardobusetti.unibztimetable.ui.settings.SettingsFragment
import com.riccardobusetti.unibztimetable.ui.timemachine.TimeMachineFragment
import com.riccardobusetti.unibztimetable.ui.today.TodayFragment
import com.riccardobusetti.unibztimetable.utils.NotificationUtils
import com.riccardobusetti.unibztimetable.utils.custom.views.NonSwipeableViewPager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    data class AppFragment(
        val index: Int,
        val itemId: Int,
        val fragment: Fragment
    )

    data class AndroidNotificationChannel(
        val id: String,
        val name: String,
        val description: String
    )

    private val appFragments = listOf(
        AppFragment(
            0,
            R.id.action_today,
            TodayFragment()
        ),
        AppFragment(
            1,
            R.id.action_next_7_days,
            Next7DaysFragment()
        ),
        AppFragment(
            2,
            R.id.action_time_machine,
            TimeMachineFragment()
        ),
        AppFragment(
            3,
            R.id.action_canteen,
            CanteenFragment()
        ),
        AppFragment(
            4,
            R.id.action_settings,
            SettingsFragment()
        )
    )

    private val notificationChannels = listOf(
        AndroidNotificationChannel(
            NotificationUtils.DAILY_UPDATES_CHANNEL_ID,
            "Daily updates",
            "This channel contains all the daily updates from the app."
        )
    )

    private lateinit var viewPager: NonSwipeableViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannels()
        setupUi()
        attachListeners()

        val alarmMgr = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, UpdateTodayTimetableIntentService::class.java).let { intent ->
            PendingIntent.getBroadcast(this, 0, intent, 0)
        }

        alarmMgr.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 5 * 1000,
            alarmIntent
        )
    }

    private fun setupUi() {
        viewPager = activity_main_view_pager
        viewPager.offscreenPageLimit = appFragments.size
        viewPager.adapter = FragmentsAdapter(appFragments, supportFragmentManager)
        viewPager.currentItem = appFragments[0].index
    }

    private fun attachListeners() {
        activity_main_bottom_navigation.setOnNavigationItemSelectedListener { menuItem ->
            viewPager.currentItem = appFragments.find { it.itemId == menuItem.itemId }!!.index

            true
        }
    }

    private fun createNotificationChannels() {
        notificationChannels.forEach {
            NotificationUtils.createNotificationChannel(this, it)
        }
    }
}
