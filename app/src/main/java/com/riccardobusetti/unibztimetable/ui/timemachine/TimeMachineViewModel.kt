package com.riccardobusetti.unibztimetable.ui.timemachine

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.Day
import com.riccardobusetti.unibztimetable.domain.entities.UserPrefs
import com.riccardobusetti.unibztimetable.domain.usecases.GetIntervalDateTimetableUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.GetUserPrefsUseCase
import com.riccardobusetti.unibztimetable.utils.DateUtils
import com.riccardobusetti.unibztimetable.utils.custom.TimetableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class TimeMachineViewModel(
    private val context: Context,
    private val getIntervalDateTimetableUseCase: GetIntervalDateTimetableUseCase,
    private val getUserPrefsUseCase: GetUserPrefsUseCase
) : TimetableViewModel<List<Day>>() {

    /**
     * Enum representing the state of the bottomsheet which pops up when the user
     * needs to check for an interval of dates in order to perform a time travel.
     */
    enum class BottomSheetState {
        OPENED,
        CLOSED
    }

    /**
     * Enum representing the status of the date picker which will be
     * triggered inside of the bottom sheet for the time travel.
     */
    enum class DatePickerState {
        CLOSED,
        OPENED_FOR_FROM_DATE,
        OPENED_FOR_TO_DATE
    }

    companion object {
        private const val TAG = "TimeMachineViewModel"
    }

    val selectedDateInterval = MutableLiveData<Pair<String, String>>().apply {
        this.value =
            DateUtils.getCurrentDateFormatted() to DateUtils.getCurrentDatePlusYearsFormatted(1)
    }

    val bottomSheetState =
        MutableLiveData<BottomSheetState>().apply { this.value = BottomSheetState.CLOSED }

    val datePickerState =
        MutableLiveData<DatePickerState>().apply { this.value = DatePickerState.CLOSED }

    fun loadTimetable(
        fromDate: String,
        toDate: String,
        page: String
    ) {
        viewModelScope.launchWithSupervisor {
            loadingState.value = true

            val userPrefs = withContext(Dispatchers.IO) {
                getUserPrefsUseCase.getUserPrefs()
            }

            val work = async(Dispatchers.IO) {
                getIntervalDateTimetableUseCase.getTimetable(
                    userPrefs.prefs[UserPrefs.Pref.DEPARTMENT_ID] ?: "",
                    userPrefs.prefs[UserPrefs.Pref.DEGREE_ID] ?: "",
                    userPrefs.prefs[UserPrefs.Pref.STUDY_PLAN_ID] ?: "",
                    fromDate,
                    toDate,
                    page
                )
            }

            val newTimetable = try {
                work.await()
            } catch (e: Exception) {
                Log.d(TAG, "This error occurred while parsing the timetable -> $e")

                error.value = context.getString(R.string.error_fetching)

                null
            }

            loadingState.value = false
            newTimetable?.let {
                if (newTimetable.isEmpty())
                    error.value = context.getString(R.string.error_no_courses)
                else
                    error.value = NO_ERROR
                    timetable.value = newTimetable
            }
        }
    }
}