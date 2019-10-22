package com.riccardobusetti.unibztimetable.ui.next7days

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
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository
import com.riccardobusetti.unibztimetable.domain.repositories.UserPrefsRepository
import com.riccardobusetti.unibztimetable.domain.strategies.RemoteTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.SharedPreferencesUserPrefsStrategy
import com.riccardobusetti.unibztimetable.domain.usecases.GetNext7DaysTimetableUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.GetUserPrefsUseCase
import com.riccardobusetti.unibztimetable.ui.items.CourseItem
import com.riccardobusetti.unibztimetable.ui.items.DayItem
import com.riccardobusetti.unibztimetable.utils.custom.AdvancedFragment
import com.riccardobusetti.unibztimetable.utils.custom.TimetableViewModel
import com.riccardobusetti.unibztimetable.utils.custom.views.StatusView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.fragment_next_7_days.*

class Next7DaysFragment : AdvancedFragment<Next7DaysViewModel>() {

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private lateinit var statusView: StatusView
    private lateinit var recyclerView: RecyclerView
    private lateinit var skeleton: SkeletonScreen

    override fun initModel(): Next7DaysViewModel {
        val timetableRepository = TimetableRepository(RemoteTimetableStrategy())
        val userPrefsRepository = UserPrefsRepository(SharedPreferencesUserPrefsStrategy(context!!))

        return ViewModelProviders.of(
            this,
            Next7DaysViewModelFactory(
                context!!,
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

    override fun setupUi() {
        statusView = fragment_next_7_days_status_view

        recyclerView = fragment_next_7_days_recycler_view
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

            it.error.observe(this, Observer { error ->
                error?.let {
                    showStatusView(error)
                }
            })

            it.loadingState.observe(this, Observer { isLoading ->
                if (isLoading) {
                    hideStatusView()
                    skeleton.show()
                } else {
                    skeleton.hide()
                }
            })
        }
    }

    override fun startLoadingData() {
        model?.loadNext7DaysTimetable("1")
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