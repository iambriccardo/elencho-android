package com.riccardobusetti.unibztimetable.ui.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.Day
import com.riccardobusetti.unibztimetable.domain.usecases.GetIntervalDateTimetableUseCase
import com.riccardobusetti.unibztimetable.ui.utils.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class TimeMachineViewModel(
    private val context: Context,
    private val intervalDateUseCase: GetIntervalDateTimetableUseCase
) : ViewModel() {

    val dateInterval = MutableLiveData<Pair<String, String>>().apply {
        this.value =
            DateUtils.getCurrentDateFormatted() to DateUtils.getCurrentDatePlusYearsFormatted(1)
    }

    val error = MutableLiveData<String>()

    val loading = MutableLiveData<Boolean>()

    val timetable = MutableLiveData<List<Day>>()

    fun loadTimetable(
        department: String,
        degree: String,
        academicYear: String,
        fromDate: String,
        toDate: String,
        page: String
    ) {
        viewModelScope.launchWithSupervisor {
            loading.value = true

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

            loading.value = false
            newTimetable?.let {
                timetable.value = newTimetable
            }
        }
    }
}