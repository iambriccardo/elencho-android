package com.riccardobusetti.unibztimetable

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.riccardobusetti.unibztimetable.ui.fragments.Next7DaysFragment
import com.riccardobusetti.unibztimetable.ui.fragments.TodayFragment
import com.riccardobusetti.unibztimetable.ui.fragments.YearlyFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val fragmentsMap = mapOf(
        R.id.action_today to TodayFragment(),
        R.id.action_next_7_days to Next7DaysFragment(),
        R.id.action_yearly to YearlyFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUi()
        attachListeners()
    }

    private fun setupUi() {
        mountFragment(TodayFragment())
    }

    private fun attachListeners() {
        activity_main_bottom_navigation.setOnNavigationItemSelectedListener {
            fragmentsMap[it.itemId]?.let { fragment ->
                mountFragment(fragment)
            }

            true
        }
    }

    private fun mountFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_main_fragment_container, fragment)
            .commit()
    }
}
