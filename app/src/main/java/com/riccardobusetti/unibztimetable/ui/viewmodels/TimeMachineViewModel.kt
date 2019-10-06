package com.riccardobusetti.unibztimetable.ui.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.usecases.GetIntervalDateTimetableUseCase
import com.riccardobusetti.unibztimetable.ui.utils.DateUtils
import com.riccardobusetti.unibztimetable.ui.utils.components.TimetableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class TimeMachineViewModel(
    private val context: Context,
    private val intervalDateUseCase: GetIntervalDateTimetableUseCase
) : TimetableViewModel() {

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
                error.value = context.getString(R.string.error_fetching)
                null
            }

            loadingState.value = false
            newTimetable?.let {
                timetable.value = newTimetable
            }
        }
    }
}