package com.riccardobusetti.unibztimetable.ui.today

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.riccardobusetti.unibztimetable.domain.entities.Day
import com.riccardobusetti.unibztimetable.domain.entities.UserPrefs
import com.riccardobusetti.unibztimetable.domain.usecases.GetTodayTimetableUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.GetUserPrefsUseCase
import com.riccardobusetti.unibztimetable.utils.custom.TimetableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class TodayViewModel(
    private val context: Context,
    private val getTodayTimetableUseCase: GetTodayTimetableUseCase,
    private val getUserPrefsUseCase: GetUserPrefsUseCase
) : TimetableViewModel<List<Day>>() {

    companion object {
        private const val TAG = "TodayViewModel"
    }

    fun loadTodayTimetable(page: String = DEFAULT_PAGE) {
        viewModelScope.launchWithSupervisor {
            loadingState.value = true

            val userPrefs = withContext(Dispatchers.IO) {
                getUserPrefsUseCase.getUserPrefs()
            }

            val work = async(Dispatchers.IO) {
                getTodayTimetableUseCase.getTodayTimetable(
                    userPrefs.prefs[UserPrefs.Pref.DEPARTMENT_ID] ?: "",
                    userPrefs.prefs[UserPrefs.Pref.DEGREE_ID] ?: "",
                    userPrefs.prefs[UserPrefs.Pref.STUDY_PLAN_ID] ?: "",
                    page
                )
            }

            val newTimetable = try {
                work.await()
            } catch (e: Exception) {
                Log.d(TAG, "This error occurred while parsing the timetable -> $e")

                error.value = TimetableError.ERROR_WHILE_GETTING_TIMETABLE

                null
            }

            loadingState.value = false
            newTimetable?.let {
                if (newTimetable.isEmpty())
                    error.value = TimetableError.EMPTY_TIMETABLE
                else
                    error.value = null
                    timetable.value = newTimetable
            }
        }
    }
}