package com.riccardobusetti.unibztimetable.ui.timemachine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository
import com.riccardobusetti.unibztimetable.domain.repositories.UserPrefsRepository
import com.riccardobusetti.unibztimetable.domain.strategies.RemoteTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.SharedPreferencesUserPrefsStrategy
import com.riccardobusetti.unibztimetable.domain.usecases.GetIntervalDateTimetableUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.GetUserPrefsUseCase
import com.riccardobusetti.unibztimetable.utils.DateUtils
import com.riccardobusetti.unibztimetable.utils.custom.AdvancedFragment
import com.riccardobusetti.unibztimetable.utils.custom.TimetableViewModel
import com.riccardobusetti.unibztimetable.utils.custom.views.StatusView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.bottom_sheet_date_interval.view.*
import kotlinx.android.synthetic.main.fragment_time_machine.*

class TimeMachineFragment : AdvancedFragment<TimeMachineViewModel>() {

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private lateinit var bottomSheetView: View
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var statusView: StatusView
    private lateinit var fromDateText: TextView
    private lateinit var toDateText: TextView
    private lateinit var timeTravelButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var skeleton: SkeletonScreen

    override fun initModel(): TimeMachineViewModel {
        val timetableRepository = TimetableRepository(RemoteTimetableStrategy())

        val userPrefsRepository = UserPrefsRepository(SharedPreferencesUserPrefsStrategy(context!!))

        return ViewModelProviders.of(
            this,
            TimeMachineViewModelFactory(
                context!!,
                GetIntervalDateTimetableUseCase(timetableRepository),
                GetUserPrefsUseCase(userPrefsRepository)
            )
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
        bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_date_interval, null)

        bottomSheetDialog = BottomSheetDialog(context!!)
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.setOnCancelListener { changeBottomSheetState() }

        statusView = fragment_time_machine_status_view

        fromDateText = bottomSheetView.bottom_sheet_date_interval_from_text
        fromDateText.setOnClickListener {
            MaterialDialog(context!!).show {
                datePicker(
                    currentDate = model?.getCurrentFromDate()
                ) { _, date ->
                    model?.updateFromDate(DateUtils.formatDateToString(date.time))
                }
            }
        }

        toDateText = bottomSheetView.bottom_sheet_date_interval_to_text
        toDateText.setOnClickListener {
            MaterialDialog(context!!).show {
                datePicker(
                    currentDate = model?.getCurrentToDate()
                ) { _, date ->
                    model?.updateToDate(DateUtils.formatDateToString(date.time))
                }
            }
        }

        timeTravelButton = bottomSheetView.bottom_sheet_date_interval_button
        timeTravelButton.setOnClickListener { _ ->
            model?.let {
                it.currentPage.value = TimetableViewModel.DEFAULT_PAGE
            }

            changeBottomSheetState()
        }

        recyclerView = fragment_time_machine_recycler_view
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = groupAdapter
            onEndReached { currentPage ->
                model?.let {
                    it.currentPage.value = currentPage
                }
            }
        }

        floatingActionButton = fragment_time_machine_fab
        floatingActionButton.setOnClickListener { changeBottomSheetState() }

        skeleton = Skeleton.bind(recyclerView)
            .adapter(groupAdapter)
            .load(R.layout.item_skeleton)
            .color(R.color.colorSkeletonShimmer)
            .show()
    }

    override fun attachObservers() {
        model?.let {
            it.timetable.observe(this, Observer { timetable ->
                groupAdapter.apply {
                    if (model?.isCurrentPageFirstPage()!!) clear()
                    addTimetable(timetable)
                }
            })

            it.error.observe(this, Observer { error ->
                error?.let {
                    showStatusView(error)
                }
            })

            it.loadingState.observe(this, Observer { isLoading ->
                if (model?.isCurrentPageFirstPage()!!) {
                    if (isLoading) {
                        hideStatusView()
                        skeleton.show()
                    } else {
                        skeleton.hide()
                    }
                } else if (isLoading) {
                    Toast.makeText(context!!, R.string.loading_new_page, Toast.LENGTH_SHORT).show()
                }
            })

            it.currentPage.observe(this, Observer { currentPage ->
                // TODO: develop a way to put all the load calls inside of a queue of execution.
                model?.loadTimetable(
                    model?.selectedDateInterval?.value!!.first,
                    model?.selectedDateInterval?.value!!.second,
                    currentPage
                )
            })

            it.selectedDateInterval.observe(this, Observer { interval ->
                fromDateText.text = interval.first
                toDateText.text = interval.second
            })

            it.bottomSheetState.observe(this, Observer { bottomSheetState ->
                when (bottomSheetState) {
                    TimeMachineViewModel.BottomSheetState.OPENED -> bottomSheetDialog.show()
                    TimeMachineViewModel.BottomSheetState.CLOSED -> bottomSheetDialog.hide()
                    else -> bottomSheetDialog.hide()
                }
            })
        }
    }

    override fun startLoadingData() {
        // This method is not needed for this particular use case.
    }

    private fun changeBottomSheetState() {
        model?.bottomSheetState?.value = when (model?.bottomSheetState?.value) {
            TimeMachineViewModel.BottomSheetState.CLOSED -> TimeMachineViewModel.BottomSheetState.OPENED
            TimeMachineViewModel.BottomSheetState.OPENED -> TimeMachineViewModel.BottomSheetState.CLOSED
            null -> TimeMachineViewModel.BottomSheetState.CLOSED
        }
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