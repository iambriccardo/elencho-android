package com.riccardobusetti.unibztimetable.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.ui.adapters.FragmentsAdapter
import com.riccardobusetti.unibztimetable.ui.fragments.Next7DaysFragment
import com.riccardobusetti.unibztimetable.ui.fragments.TimeMachineFragment
import com.riccardobusetti.unibztimetable.ui.fragments.TodayFragment
import com.riccardobusetti.unibztimetable.ui.utils.views.NonSwipeableViewPager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    data class IndexableFragment(val index: Int, val itemId: Int, val fragment: Fragment)

    private val indexableFragments = listOf(
        IndexableFragment(0, R.id.action_today, TodayFragment()),
        IndexableFragment(1, R.id.action_next_7_days, Next7DaysFragment()),
        IndexableFragment(2, R.id.action_time_machine, TimeMachineFragment())
    )

    private lateinit var viewPager: NonSwipeableViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUi()
        attachListeners()
    }

    private fun setupUi() {
        viewPager = activity_main_view_pager
        viewPager.offscreenPageLimit = indexableFragments.size
        viewPager.adapter = FragmentsAdapter(indexableFragments, supportFragmentManager)
        viewPager.currentItem = indexableFragments[0].index
    }

    private fun attachListeners() {
        activity_main_bottom_navigation.setOnNavigationItemSelectedListener { menuItem ->
            viewPager.currentItem = indexableFragments.find { it.itemId == menuItem.itemId }!!.index

            true
        }
    }
}
