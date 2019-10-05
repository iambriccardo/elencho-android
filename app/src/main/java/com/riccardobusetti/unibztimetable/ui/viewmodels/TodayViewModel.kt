package com.riccardobusetti.unibztimetable.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riccardobusetti.unibztimetable.domain.entities.Day
import com.riccardobusetti.unibztimetable.domain.usecases.GetTodayTimetableUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodayViewModel(private val todayUseCase: GetTodayTimetableUseCase) : ViewModel() {

    val loading = MutableLiveData<Boolean>().apply { this.value = false }

    val timetable = MutableLiveData<List<Day>>().apply { this.value = mutableListOf() }

    fun loadTodayTimetable(department: String, degree: String, academicYear: String, page: String) {
        viewModelScope.launch {
            loading.value = true

            val newTimetable = withContext(Dispatchers.IO) {
                todayUseCase.getTodayTimetable(
                    department,
                    degree,
                    academicYear,
                    page
                )
            }

            loading.value = false
            timetable.value = newTimetable
        }
    }
}