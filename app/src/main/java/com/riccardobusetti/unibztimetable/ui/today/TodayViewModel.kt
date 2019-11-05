package com.riccardobusetti.unibztimetable.ui.today

import androidx.lifecycle.viewModelScope
import com.riccardobusetti.unibztimetable.domain.entities.UserPrefs
import com.riccardobusetti.unibztimetable.domain.usecases.GetTodayTimetableUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.GetUserPrefsUseCase
import com.riccardobusetti.unibztimetable.utils.custom.TimetableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TodayViewModel(
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

            val userPrefs = getUserPrefs()
            val newTimetable = try {
                loadTimetable(userPrefs, page)
            } catch (e: Exception) {
                handleTimetableException(TAG, e)
            }

            hideLoading()
            showTimetable(newTimetable)
        }
    }

    private suspend fun getUserPrefs() = withContext(Dispatchers.IO) {
        getUserPrefsUseCase.getUserPrefs()
    }

    private suspend fun loadTimetable(
        userPrefs: UserPrefs,
        page: String
    ) = withContext(Dispatchers.IO) {
        getTodayTimetableUseCase.getTodayTimetableWithOnGoingCourse(
            userPrefs.prefs[UserPrefs.Pref.DEPARTMENT_ID] ?: "",
            userPrefs.prefs[UserPrefs.Pref.DEGREE_ID] ?: "",
            userPrefs.prefs[UserPrefs.Pref.STUDY_PLAN_ID] ?: "",
            page
        )
    }
}