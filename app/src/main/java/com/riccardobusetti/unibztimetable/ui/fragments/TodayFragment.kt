package com.riccardobusetti.unibztimetable.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository
import com.riccardobusetti.unibztimetable.domain.strategies.CachedTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.RemoteTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.usecases.GetTodayTimetableUseCase
import com.riccardobusetti.unibztimetable.ui.items.CourseItem
import com.riccardobusetti.unibztimetable.ui.items.DayItem
import com.riccardobusetti.unibztimetable.ui.utils.AdvancedFragment
import com.riccardobusetti.unibztimetable.ui.viewmodels.TodayViewModel
import com.riccardobusetti.unibztimetable.ui.viewmodels.factories.TodayViewModelFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.fragment_today.*

class TodayFragment : AdvancedFragment<TodayViewModel>() {

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun initModel(): TodayViewModel {
        val repository = TimetableRepository(
            RemoteTimetableStrategy(),
            CachedTimetableStrategy()
        )

        return ViewModelProviders.of(
            this,
            TodayViewModelFactory(GetTodayTimetableUseCase(repository))
        ).get(TodayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_today, container, false)
    }

    override fun setupUi() {
        recyclerView = fragment_today_recycler_view
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = groupAdapter
        }

        progressBar = fragment_today_progress_bar
    }

    override fun attachObservers() {
        model?.let {
            it.loading.observe(this, Observer { isLoading ->
                progressBar.visibility = when (isLoading) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
            })

            it.timetable.observe(this, Observer { timetable ->
                groupAdapter.clear()

                timetable.forEach { day ->
                    val section = Section()
                    section.setHeader(DayItem(day))

                    day.courses.forEach { course ->
                        section.add(CourseItem(course))
                    }

                    groupAdapter.add(section)
                }
            })
        }
    }

    override fun startLoadingData() {
        model?.loadTodayTimetable(
            "22",
            "13205",
            "16858",
            "1"
        )
    }

}