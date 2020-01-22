package com.riccardobusetti.unibztimetable.ui.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.AppSection
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository
import com.riccardobusetti.unibztimetable.domain.repositories.UserPrefsRepository
import com.riccardobusetti.unibztimetable.domain.strategies.LocalTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.RemoteTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.SharedPreferencesUserPrefsStrategy
import com.riccardobusetti.unibztimetable.domain.usecases.GetTodayTimetableUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.GetUserPrefsUseCase
import com.riccardobusetti.unibztimetable.ui.custom.TimetableFragment
import com.riccardobusetti.unibztimetable.ui.custom.TimetableViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_today.*


class TodayFragment : TimetableFragment<TodayViewModel>() {

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    override val appSection: AppSection
        get() = AppSection.TODAY

    override fun initViewModel(): TodayViewModel {
        val timetableRepository =
            TimetableRepository(
                LocalTimetableStrategy(activity!!.applicationContext),
                RemoteTimetableStrategy()
            )
        val userPrefsRepository = UserPrefsRepository(SharedPreferencesUserPrefsStrategy(context!!))

        return ViewModelProviders.of(
            this,
            TodayViewModelFactory(
                context!!,
                GetTodayTimetableUseCase(timetableRepository),
                GetUserPrefsUseCase(userPrefsRepository)
            )
        ).get(TodayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_today, container, false)
    }

    override fun onResume() {
        super.onResume()

        model?.let {
            if (!it.isFirstLaunch) {
                loadTimetable()
            }

            it.isFirstLaunch = false
        }
    }

    override fun setupUI() {
        parentLayout = fragment_today_parent

        loadingView = fragment_today_lottie_loading_view

        statusView = fragment_today_status_view

        swipeToRefreshLayout = fragment_today_swipe_refresh

        recyclerView = fragment_today_recycler_view
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = groupAdapter
        }
    }

    override fun attachObservers() {
        model?.let {
            it.timetable.observe(this, Observer { timetable ->
                recyclerView?.let { recyclerView ->
                    groupAdapter.clearAndAddTimetable(timetable, recyclerView)
                }
            })

            it.error.observe(this, Observer { error ->
                if (error != null) {
                    showError(error)
                } else {
                    hideError()
                }
            })

            it.loadingState.observe(this, Observer { loadingState ->
                when (loadingState) {
                    TimetableViewModel.TimetableLoadingState.LOADING_FROM_SCRATCH -> showLoadingView()
                    TimetableViewModel.TimetableLoadingState.LOADING_WITH_DATA -> {
                    }
                    TimetableViewModel.TimetableLoadingState.NOT_LOADING -> hideLoadingView()
                    else -> hideLoadingView()
                }
            })
        }
    }

    override fun loadData() {
        loadTimetable()
    }

    override fun reloadData() {
        reloadTimetable()
    }

    private fun loadTimetable() {
        model?.requestTimetable(
            TimetableViewModel.TimetableRequest(
                page = model!!.currentPage,
                isReset = true
            )
        )
    }

    private fun reloadTimetable() {
        model?.let {
            recyclerView?.scrollToPosition(0)
            it.enableListAnimation()
            loadTimetable()
        }
    }
}