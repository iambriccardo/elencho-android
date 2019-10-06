package com.riccardobusetti.unibztimetable.ui.viewmodels.factories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riccardobusetti.unibztimetable.domain.usecases.GetIntervalDateTimetableUseCase

class TimeMachineViewModelFactory(
    private val context: Context,
    private val intervalDateUseCase: GetIntervalDateTimetableUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            Context::class.java,
            GetIntervalDateTimetableUseCase::class.java
        )
            .newInstance(context, intervalDateUseCase)
    }
}