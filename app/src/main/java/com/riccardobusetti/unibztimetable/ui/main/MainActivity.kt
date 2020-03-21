package com.riccardobusetti.unibztimetable.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.app.AppFragment
import com.riccardobusetti.unibztimetable.ui.adapters.MainFragmentsAdapter
import com.riccardobusetti.unibztimetable.ui.canteen.CanteenFragment
import com.riccardobusetti.unibztimetable.ui.custom.FragmentedActivity
import com.riccardobusetti.unibztimetable.ui.next7days.Next7DaysFragment
import com.riccardobusetti.unibztimetable.ui.roomcheck.RoomCheckFragment
import com.riccardobusetti.unibztimetable.ui.settings.SettingsFragment
import com.riccardobusetti.unibztimetable.ui.timemachine.TimeMachineFragment
import com.riccardobusetti.unibztimetable.ui.today.TodayFragment
import com.riccardobusetti.unibztimetable.utils.NotificationUtils
import com.riccardobusetti.unibztimetable.utils.custom.views.NonSwipeableViewPager
import com.riccardobusetti.unibztimetable.workers.TodayTimetableUpdateWorker
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    data class AndroidNotificationChannel(
        val id: String,
        val name: String,
        val description: String
    )

    private val mainFragments: List<AppFragment>
        get() = listOf(
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

    private val notificationChannels: List<AndroidNotificationChannel>
        get() = listOf(
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
        setupUI()
        attachListeners()
        startWorkers()
    }

    private fun setupUI() {
        val toolbar = activity_main_toolbar
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.app_name)
        toolbar.setTitleTextAppearance(this, R.style.LogoTextAppearance)

        viewPager = activity_main_view_pager
        viewPager.offscreenPageLimit = mainFragments.size
        viewPager.adapter = MainFragmentsAdapter(mainFragments, supportFragmentManager)
        viewPager.currentItem = mainFragments[0].index
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_room_check -> {
                val fragment = RoomCheckFragment()
                fragment.show(supportFragmentManager, "tag")
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun attachListeners() {
        activity_main_bottom_navigation.setOnNavigationItemSelectedListener { menuItem ->
            viewPager.currentItem = mainFragments.find { it.itemId == menuItem.itemId }!!.index

            true
        }
    }

    private fun createNotificationChannels() {
        notificationChannels.forEach {
            NotificationUtils.createNotificationChannel(this, it)
        }
    }

    private fun startWorkers() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val todayTimetableUpdateRequest =
            PeriodicWorkRequestBuilder<TodayTimetableUpdateWorker>(3, TimeUnit.HOURS)
                .setConstraints(constraints)
                .addTag(TodayTimetableUpdateWorker.TAG)
                .build()

        val workManager = WorkManager.getInstance(this)

        if (!workManager.isAnyWorkScheduled(TodayTimetableUpdateWorker.TAG)) {
            workManager.enqueue(todayTimetableUpdateRequest)
        }
    }

    private fun WorkManager.isAnyWorkScheduled(tag: String): Boolean {
        return try {
            getWorkInfosByTag(tag).get().firstOrNull { !it.state.isFinished } != null
        } catch (e: Exception) {
            when (e) {
                is ExecutionException, is InterruptedException -> {
                    e.printStackTrace()
                }
                else -> throw e
            }
            false
        }
    }
}
