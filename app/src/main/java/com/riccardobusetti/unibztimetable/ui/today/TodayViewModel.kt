package com.riccardobusetti.unibztimetable.ui.today

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.riccardobusetti.unibztimetable.domain.entities.UserPrefs
import com.riccardobusetti.unibztimetable.domain.entities.safeGet
import com.riccardobusetti.unibztimetable.domain.usecases.GetTodayTimetableUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.GetUserPrefsUseCase
import com.riccardobusetti.unibztimetable.utils.NetworkUtils
import com.riccardobusetti.unibztimetable.utils.custom.TimetableViewModel
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

    fun loadTodayTimetable(page: String = DEFAULT_PAGE) {
        viewModelScope.launchWithSupervisor {
            hideError()
            showLoading()

            loadTimetable(getUserPrefs(), page)
                .flowOn(Dispatchers.IO)
                .onEach {
                    hideLoading()
                    showTimetable(it)
                }
                .handleErrors(TAG)
                .collect()
        }
    }

    private suspend fun getUserPrefs() = withContext(Dispatchers.IO) {
        getUserPrefsUseCase.getUserPrefs()
    }

    private fun loadTimetable(
        userPrefs: UserPrefs,
        page: String
    ) = getTodayTimetableUseCase.getTodayTimetableWithOnGoingCourse(
        userPrefs.prefs.safeGet(UserPrefs.Pref.DEPARTMENT_ID),
        userPrefs.prefs.safeGet(UserPrefs.Pref.DEGREE_ID),
        userPrefs.prefs.safeGet(UserPrefs.Pref.STUDY_PLAN_ID),
        page,
        NetworkUtils.isConnectedToInternet(context)
    )
}