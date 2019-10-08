package com.riccardobusetti.unibztimetable.ui.timemachine

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.Day
import com.riccardobusetti.unibztimetable.domain.usecases.GetIntervalDateTimetableUseCase
import com.riccardobusetti.unibztimetable.utils.DateUtils
import com.riccardobusetti.unibztimetable.utils.components.TimetableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class TimeMachineViewModel(
    private val context: Context,
    private val intervalDateUseCase: GetIntervalDateTimetableUseCase
) : TimetableViewModel<List<Day>>() {

    companion object {
        private const val TAG = "TimeMachineViewModel"
    }

    val selectedDateInterval = MutableLiveData<Pair<String, String>>().apply {
        this.value =
            DateUtils.getCurrentDateFormatted() to DateUtils.getCurrentDatePlusYearsFormatted(1)
    }

    val bottomSheetState = MutableLiveData<Boolean>().apply { this.value = false }

    fun loadTimetable(
        department: String,
        degree: String,
        academicYear: String,
        fromDate: String,
        toDate: String,
        page: String
    ) {
        viewModelScope.launchWithSupervisor {
            loadingState.value = true

            val work = async(Dispatchers.IO) {
                intervalDateUseCase.getTimetable(
                    department,
                    degree,
                    academicYear,
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
                    timetable.value = newTimetable
            }
        }
    }
}