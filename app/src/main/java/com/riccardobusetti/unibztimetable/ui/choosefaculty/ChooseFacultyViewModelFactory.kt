package com.riccardobusetti.unibztimetable.ui.choosefaculty

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riccardobusetti.unibztimetable.domain.repositories.ChooseFacultyRepository
import com.riccardobusetti.unibztimetable.domain.usecases.PutUserPrefsUseCase

class ChooseFacultyViewModelFactory(
    private val context: Context,
    private val repository: ChooseFacultyRepository,
    private val putUserPrefsUseCase: PutUserPrefsUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            Context::class.java,
            ChooseFacultyRepository::class.java,
            PutUserPrefsUseCase::class.java
        ).newInstance(context, repository, putUserPrefsUseCase)
    }
}