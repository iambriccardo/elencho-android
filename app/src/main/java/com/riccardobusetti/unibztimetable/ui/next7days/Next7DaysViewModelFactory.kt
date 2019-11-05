package com.riccardobusetti.unibztimetable.ui.next7days

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riccardobusetti.unibztimetable.domain.usecases.GetNext7DaysTimetableUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.GetUserPrefsUseCase

class Next7DaysViewModelFactory(
    private val getNext7DaysTimetableUseCase: GetNext7DaysTimetableUseCase,
    private val getUserPrefsUseCase: GetUserPrefsUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            GetNext7DaysTimetableUseCase::class.java,
            GetUserPrefsUseCase::class.java
        )
            .newInstance(getNext7DaysTimetableUseCase, getUserPrefsUseCase)
    }
}