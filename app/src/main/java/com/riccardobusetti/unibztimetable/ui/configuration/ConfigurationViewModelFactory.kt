package com.riccardobusetti.unibztimetable.ui.configuration

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riccardobusetti.unibztimetable.domain.usecases.DeleteLocalTimetableUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.PutUserPrefsUseCase

class ConfigurationViewModelFactory(
    private val context: Context,
    private val putUserPrefsUseCase: PutUserPrefsUseCase,
    private val deleteLocalTimetableUseCase: DeleteLocalTimetableUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            Context::class.java,
            PutUserPrefsUseCase::class.java,
            DeleteLocalTimetableUseCase::class.java
        ).newInstance(context, putUserPrefsUseCase, deleteLocalTimetableUseCase)
    }
}