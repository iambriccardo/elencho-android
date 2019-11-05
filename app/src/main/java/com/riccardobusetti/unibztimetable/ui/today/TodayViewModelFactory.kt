package com.riccardobusetti.unibztimetable.ui.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riccardobusetti.unibztimetable.domain.usecases.GetTodayTimetableUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.GetUserPrefsUseCase

class TodayViewModelFactory(
    private val getTodayTimetableUseCase: GetTodayTimetableUseCase,
    private val getUserPrefsUseCase: GetUserPrefsUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            GetTodayTimetableUseCase::class.java,
            GetUserPrefsUseCase::class.java
        )
            .newInstance(getTodayTimetableUseCase, getUserPrefsUseCase)
    }
}