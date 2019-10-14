package com.riccardobusetti.unibztimetable.ui.configuration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ConfigurationViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor().newInstance()
    }
}