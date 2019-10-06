package com.riccardobusetti.unibztimetable.ui.next7days

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.usecases.GetNext7DaysTimetableUseCase
import com.riccardobusetti.unibztimetable.utils.components.TimetableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class Next7DaysViewModel(
    private val context: Context,
    private val next7DaysUseCase: GetNext7DaysTimetableUseCase
) : TimetableViewModel() {

    fun loadNext7DaysTimetable(
        department: String,
        degree: String,
        academicYear: String,
        page: String
    ) {
        viewModelScope.launchWithSupervisor {
            loadingState.value = true

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