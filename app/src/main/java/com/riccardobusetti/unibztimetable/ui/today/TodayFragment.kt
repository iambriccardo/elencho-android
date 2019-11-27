package com.riccardobusetti.unibztimetable.ui.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.AppSection
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository
import com.riccardobusetti.unibztimetable.domain.repositories.UserPrefsRepository
import com.riccardobusetti.unibztimetable.domain.strategies.LocalTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.RemoteTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.SharedPreferencesUserPrefsStrategy
import com.riccardobusetti.unibztimetable.domain.usecases.GetTodayTimetableUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.GetUserPrefsUseCase
import com.riccardobusetti.unibztimetable.utils.ColorUtils
import com.riccardobusetti.unibztimetable.utils.custom.AdvancedFragment
import com.riccardobusetti.unibztimetable.utils.custom.TimetableViewModel
import com.riccardobusetti.unibztimetable.utils.custom.views.StatusView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_today.*

class TodayFragment : AdvancedFragment<TodayViewModel>() {

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private lateinit var statusView: StatusView
    private lateinit var recyclerView: RecyclerView
    private lateinit var skeleton: SkeletonScreen

    override val appSection: AppSection
        get() = AppSection.TODAY

    override fun initModel(): TodayViewModel {
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

        loadTimetable()
    }

    override fun setupUi() {
        statusView = fragment_today_status_view

        recyclerView = fragment_today_recycler_view
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = groupAdapter
        }

        skeleton = Skeleton.bind(recyclerView)
            .adapter(groupAdapter)
            .load(R.layout.item_skeleton)
            .color(
                ColorUtils.themeAttributeToResId(
                    context!!,
                    R.attr.colorSkeletonShimmer,
                    R.color.colorSkeletonShimmerDay
                )
            )
            .show()
    }

    override fun attachObservers() {
        model?.let {
            it.timetable.observe(this, Observer { timetable ->
                groupAdapter.clearAndAddTimetable(timetable)
            })

            it.error.observe(this, Observer { error ->
                if (error != null) {
                    showStatusView(error)
                } else {
                    hideStatusView()
                }
            })

            it.loadingState.observe(this, Observer { loadingState ->
                when (loadingState) {
                    TimetableViewModel.TimetableLoadingState.LOADING_FROM_SCRATCH,
                    TimetableViewModel.TimetableLoadingState.LOADING_WITH_DATA -> skeleton.show()
                    TimetableViewModel.TimetableLoadingState.NOT_LOADING -> skeleton.hide()
                    else -> skeleton.hide()
                }
            })
        }
    }

    override fun loadData() {}

    private fun loadTimetable() {
        model?.loadTodayTimetable(model?.currentPage?.value!!)
    }

    private fun showStatusView(error: TimetableViewModel.TimetableError) {
        statusView.setError(error)
        statusView.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun hideStatusView() {
        statusView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }
}