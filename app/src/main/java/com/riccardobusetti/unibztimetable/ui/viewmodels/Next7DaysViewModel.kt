package com.riccardobusetti.unibztimetable.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riccardobusetti.unibztimetable.domain.entities.Day
import com.riccardobusetti.unibztimetable.domain.usecases.GetNext7DaysTimetableUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Next7DaysViewModel(private val next7DaysUseCase: GetNext7DaysTimetableUseCase) : ViewModel() {

    val loading = MutableLiveData<Boolean>().apply { this.value = false }

    val timetable = MutableLiveData<List<Day>>().apply { this.value = mutableListOf() }

    fun loadNext7DaysTimetable(
        department: String,
        degree: String,
        academicYear: String,
        page: String
    ) {
        viewModelScope.launch {
            val newTimetable = withContext(Dispatchers.IO) {
                next7DaysUseCase.getNext7DaysTimetable(
                    department,
                    degree,
                    academicYear,
                    page
                )
            }

            timetable.value = newTimetable
        }
    }
}