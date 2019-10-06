package com.riccardobusetti.unibztimetable.ui.next7days

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riccardobusetti.unibztimetable.domain.usecases.GetNext7DaysTimetableUseCase

class Next7DaysViewModelFactory(
    private val context: Context,
    private val next7DaysUseCase: GetNext7DaysTimetableUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            Context::class.java,
            GetNext7DaysTimetableUseCase::class.java
        )
            .newInstance(context, next7DaysUseCase)
    }
}