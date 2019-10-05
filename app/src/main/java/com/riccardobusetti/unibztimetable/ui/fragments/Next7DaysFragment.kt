package com.riccardobusetti.unibztimetable.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository
import com.riccardobusetti.unibztimetable.domain.strategies.CachedTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.RemoteTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.usecases.GetNext7DaysTimetableUseCase
import com.riccardobusetti.unibztimetable.ui.viewmodels.Next7DaysViewModel
import com.riccardobusetti.unibztimetable.ui.viewmodels.factories.Next7DaysViewModelFactory

class Next7DaysFragment : ViewModelFragment<Next7DaysViewModel>() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startLoading()
        attachObservers()
    }

    private fun startLoading() {
        model?.let {
            it.loadNext7DaysTimetable(
                "22",
                "13205",
                "16858",
                "1"
            )
        }
    }

    private fun attachObservers() {
        model?.let {
            it.timetable.observe(this, Observer { timetable ->
                Log.d("NEW TIMETABLE", "$timetable")
            })
        }
    }
}