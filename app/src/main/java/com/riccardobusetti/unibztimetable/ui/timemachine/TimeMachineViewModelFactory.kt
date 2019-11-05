package com.riccardobusetti.unibztimetable.ui.timemachine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riccardobusetti.unibztimetable.domain.usecases.GetIntervalDateTimetableUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.GetUserPrefsUseCase

class TimeMachineViewModelFactory(
    private val getIntervalDateTimetableUseCase: GetIntervalDateTimetableUseCase,
    private val getUserPrefsUseCase: GetUserPrefsUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            GetIntervalDateTimetableUseCase::class.java,
            GetUserPrefsUseCase::class.java
        )
            .newInstance(getIntervalDateTimetableUseCase, getUserPrefsUseCase)
    }
}