package com.riccardobusetti.unibztimetable.ui.setup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.app.AppFragment
import com.riccardobusetti.unibztimetable.ui.adapters.SetupFragmentsAdapter
import com.riccardobusetti.unibztimetable.ui.choosefaculty.ChooseFacultyFragment
import com.riccardobusetti.unibztimetable.utils.custom.views.NonSwipeableViewPager
import kotlinx.android.synthetic.main.activity_setup.*

class SetupActivity : AppCompatActivity() {

    private val setupFragments: List<AppFragment>
        get() = listOf(
            AppFragment(
                index = 0,
                fragment = ChooseFacultyFragment()
            )
        )

    private lateinit var viewPager: NonSwipeableViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)

        setupUI()
    }

    private fun setupUI() {
        viewPager = activity_setup_view_pager
        viewPager.adapter = SetupFragmentsAdapter(setupFragments, supportFragmentManager)
    }
}