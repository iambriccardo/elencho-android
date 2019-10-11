package com.riccardobusetti.unibztimetable.ui.next7days

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riccardobusetti.unibztimetable.domain.usecases.GetNext7DaysTimetableUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.GetUserPrefsUseCase

class Next7DaysViewModelFactory(
    private val context: Context,
    private val getNext7DaysTimetableUseCase: GetNext7DaysTimetableUseCase,
    private val getUserPrefsUseCase: GetUserPrefsUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            Context::class.java,
            GetNext7DaysTimetableUseCase::class.java,
            GetUserPrefsUseCase::class.java
        )
            .newInstance(context, getNext7DaysTimetableUseCase, getUserPrefsUseCase)
    }
}