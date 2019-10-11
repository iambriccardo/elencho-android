package com.riccardobusetti.unibztimetable.ui.timemachine

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riccardobusetti.unibztimetable.domain.usecases.GetIntervalDateTimetableUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.GetUserPrefsUseCase

class TimeMachineViewModelFactory(
    private val context: Context,
    private val getIntervalDateTimetableUseCase: GetIntervalDateTimetableUseCase,
    private val getUserPrefsUseCase: GetUserPrefsUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            Context::class.java,
            GetIntervalDateTimetableUseCase::class.java,
            GetUserPrefsUseCase::class.java
        )
            .newInstance(context, getIntervalDateTimetableUseCase, getUserPrefsUseCase)
    }
}