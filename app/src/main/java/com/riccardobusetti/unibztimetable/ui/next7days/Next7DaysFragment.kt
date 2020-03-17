package com.riccardobusetti.unibztimetable.ui.next7days

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.app.AppSection
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository
import com.riccardobusetti.unibztimetable.domain.repositories.UserPrefsRepository
import com.riccardobusetti.unibztimetable.domain.strategies.LocalTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.RemoteTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.SharedPreferencesUserPrefsStrategy
import com.riccardobusetti.unibztimetable.domain.usecases.GetNext7DaysTimetableUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.GetUserPrefsUseCase
import com.riccardobusetti.unibztimetable.ui.custom.TimetableFragment
import com.riccardobusetti.unibztimetable.ui.custom.TimetableViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_next_7_days.*

class Next7DaysFragment : TimetableFragment<Next7DaysViewModel>() {

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    override val appSection: AppSection
        get() = AppSection.NEXT_7_DAYS

    override fun initViewModel(): Next7DaysViewModel {
        val timetableRepository =
            TimetableRepository(
                LocalTimetableStrategy(requireContext()),
                RemoteTimetableStrategy()
            )
        val userPrefsRepository =
            UserPrefsRepository(SharedPreferencesUserPrefsStrategy(requireContext()))

        return ViewModelProviders.of(
            this,
            Next7DaysViewModelFactory(
                requireContext(),
                GetNext7DaysTimetableUseCase(timetableRepository),
                GetUserPrefsUseCase(userPrefsRepository)
            )
        ).get(Next7DaysViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_next_7_days, container, false)
    }

    override fun setupUI() {
        loadingView = fragment_next_7_days_lottie_loading_view

        statusView = fragment_next_7_days_status_view

        swipeToRefreshLayout = fragment_next_7_days_swipe_refresh

        recyclerView = fragment_next_7_days_recycler_view
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = groupAdapter
            scrollListener = onEndReached(model?.listState!!) { page ->
                loadTimetableNewPage(page)
            }
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
        loadTimetable(true)
    }

    override fun reloadData() {
        reloadTimetable()
    }

    private fun loadTimetable(isReset: Boolean) {
        model?.requestTimetable(
            TimetableViewModel.TimetableRequest(
                page = model!!.currentPage,
                isReset = isReset
            )
        )
    }

    private fun reloadTimetable() {
        model?.let {
            recyclerView?.scrollToPosition(0)
            it.enableListAnimation()
            it.resetListState()
            it.updateCurrentPage(TimetableViewModel.DEFAULT_PAGE)
            loadTimetable(true)
        }
    }

    private fun loadTimetableNewPage(page: String) {
        model?.let {
            it.updateCurrentPage(page)
            loadTimetable(false)
        }
    }
}