package com.riccardobusetti.unibztimetable.ui.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riccardobusetti.unibztimetable.domain.usecases.GetTodayTimetableUseCase

class TodayViewModelFactory(private val todayUseCase: GetTodayTimetableUseCase) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(GetTodayTimetableUseCase::class.java)
            .newInstance(todayUseCase)
    }
}