package com.riccardobusetti.unibztimetable.ui.fragments

import android.os.Bundle
import android.util.Log
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
import com.riccardobusetti.unibztimetable.domain.usecases.GetNext7DaysTimetableUseCase
import com.riccardobusetti.unibztimetable.ui.items.CourseItem
import com.riccardobusetti.unibztimetable.ui.items.DayItem
import com.riccardobusetti.unibztimetable.ui.utils.AdvancedFragment
import com.riccardobusetti.unibztimetable.ui.viewmodels.Next7DaysViewModel
import com.riccardobusetti.unibztimetable.ui.viewmodels.factories.Next7DaysViewModelFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.fragment_next_7_days.*

class Next7DaysFragment : AdvancedFragment<Next7DaysViewModel>() {

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun initModel(): Next7DaysViewModel {
        val repository = TimetableRepository(
            RemoteTimetableStrategy(),
            CachedTimetableStrategy()
        )

        return ViewModelProviders.of(
            this,
            Next7DaysViewModelFactory(GetNext7DaysTimetableUseCase(repository))
        ).get(Next7DaysViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_next_7_days, container, false)
    }

    override fun setupUi() {
        recyclerView = fragment_next_7_days_recycler_view
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = groupAdapter
        }

        progressBar = fragment_next_7_days_progress_bar
    }

    override fun attachObservers() {
        model?.let {
            it.loading.observe(this, Observer { isLoading ->
                Log.d("NEXT", "isloading -> $isLoading")
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
        model?.loadNext7DaysTimetable(
            "22",
            "13205",
            "16858",
            "1"
        )
    }
}