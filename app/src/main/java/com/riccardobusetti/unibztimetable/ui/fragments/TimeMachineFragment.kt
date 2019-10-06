package com.riccardobusetti.unibztimetable.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository
import com.riccardobusetti.unibztimetable.domain.strategies.CachedTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.RemoteTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.usecases.GetIntervalDateTimetableUseCase
import com.riccardobusetti.unibztimetable.ui.items.CourseItem
import com.riccardobusetti.unibztimetable.ui.items.DayItem
import com.riccardobusetti.unibztimetable.ui.utils.AdvancedFragment
import com.riccardobusetti.unibztimetable.ui.viewmodels.TimeMachineViewModel
import com.riccardobusetti.unibztimetable.ui.viewmodels.factories.TimeMachineViewModelFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.fragment_time_machine.*

class TimeMachineFragment : AdvancedFragment<TimeMachineViewModel>() {

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private lateinit var fromDateText: TextView
    private lateinit var toDateText: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var skeleton: SkeletonScreen

    override fun initModel(): TimeMachineViewModel {
        val repository = TimetableRepository(
            RemoteTimetableStrategy(),
            CachedTimetableStrategy()
        )

        return ViewModelProviders.of(
            this,
            TimeMachineViewModelFactory(context!!, GetIntervalDateTimetableUseCase(repository))
        ).get(TimeMachineViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_time_machine, container, false)
    }

    override fun setupUi() {
        fromDateText = fragment_time_machine_from_text
        fromDateText.setOnClickListener {

        }

        toDateText = fragment_time_machine_to_text
        toDateText.setOnClickListener {

        }

        recyclerView = fragment_time_machine_recycler_view
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = groupAdapter
        }

        skeleton = Skeleton.bind(recyclerView)
            .adapter(groupAdapter)
            .load(R.layout.item_skeleton)
            .color(R.color.colorSkeletonShimmer)
            .show()
    }

    override fun attachObservers() {
        model?.let {
            it.dateInterval.observe(this, Observer { interval ->
                fromDateText.text = interval.first
                toDateText.text = interval.second

                // TODO: avoid loading when the fragment is rotated.
                model?.loadTimetable(
                    "22",
                    "13205",
                    "16858",
                    interval.first,
                    interval.second,
                    "1"
                )
            })

            it.error.observe(this, Observer { error ->
                Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
            })

            it.loading.observe(this, Observer { isLoading ->
                if (isLoading) skeleton.show() else skeleton.hide()
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
        // In this fragment we don't load data with this method
    }
}