package com.riccardobusetti.unibztimetable.ui.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.Day
import com.riccardobusetti.unibztimetable.domain.usecases.GetNext7DaysTimetableUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class Next7DaysViewModel(
    private val context: Context,
    private val next7DaysUseCase: GetNext7DaysTimetableUseCase
) : ViewModel() {

    val error = MutableLiveData<String>()

    val loading = MutableLiveData<Boolean>()

    val timetable = MutableLiveData<List<Day>>()

    fun loadNext7DaysTimetable(
        department: String,
        degree: String,
        academicYear: String,
        page: String
    ) {
        viewModelScope.launchWithSupervisor {
            loading.value = true

            val work = async(Dispatchers.IO) {
                next7DaysUseCase.getNext7DaysTimetable(
                    department,
                    degree,
                    academicYear,
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