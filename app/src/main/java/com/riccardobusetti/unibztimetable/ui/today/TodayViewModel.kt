package com.riccardobusetti.unibztimetable.ui.today

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.*
import com.riccardobusetti.unibztimetable.domain.usecases.GetTodayTimetableUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.GetUserPrefsUseCase
import com.riccardobusetti.unibztimetable.ui.custom.TimetableViewModel
import com.riccardobusetti.unibztimetable.utils.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

class TodayViewModel(
    private val context: Context,
    private val getTodayTimetableUseCase: GetTodayTimetableUseCase,
    private val getUserPrefsUseCase: GetUserPrefsUseCase
) : TimetableViewModel() {

    companion object {
        private const val TAG = "TodayViewModel"
    }

    init {
        start()
    }

    override fun start() {
        viewModelScope.launchWithSupervisor {
            for (request in timetableRequests) {
                hideError()
                showLoading()

                loadTimetable(getUserPrefs(), request.page)
                    .flowOn(Dispatchers.IO)
                    .onEach {
                        hideLoading()
                        showTimetable(it, request.isReset)
                    }
                    .handleErrors(TAG)
                    .collect()
            }
        }
    }

    private suspend fun getUserPrefs() = withContext(Dispatchers.IO) {
        getUserPrefsUseCase.getUserPrefs()
    }

    private fun loadTimetable(
        userPrefs: UserPrefs,
        page: String
    ) = getTodayTimetableUseCase.getTodayTimetableFiltered(
        userPrefs.prefs.safeGet(UserPrefs.Pref.DEPARTMENT_ID),
        userPrefs.prefs.safeGet(UserPrefs.Pref.DEGREE_ID),
        userPrefs.prefs.safeGet(UserPrefs.Pref.STUDY_PLAN_ID),
        page,
        NetworkUtils.isConnectedToInternet(context)
    )

    override fun coursesToCourseGroups(courses: List<Course>): List<DisplayableCourseGroup> {
        return DisplayableCourseGroup.build(courses, AppSection.TODAY) {
            if (it.isOngoing()) {
                context.getString(R.string.now)
            } else {
                context.getString(R.string.upcoming_lectures)
            }
        }
    }
}