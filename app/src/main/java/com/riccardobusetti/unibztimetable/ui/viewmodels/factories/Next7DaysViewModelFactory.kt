package com.riccardobusetti.unibztimetable.ui.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riccardobusetti.unibztimetable.domain.usecases.GetNext7DaysTimetableUseCase

class Next7DaysViewModelFactory(private val next7DaysUseCase: GetNext7DaysTimetableUseCase) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(GetNext7DaysTimetableUseCase::class.java)
            .newInstance(next7DaysUseCase)
    }
}