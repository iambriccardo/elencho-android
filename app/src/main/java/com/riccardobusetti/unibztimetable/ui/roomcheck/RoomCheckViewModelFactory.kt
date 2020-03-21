package com.riccardobusetti.unibztimetable.ui.roomcheck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RoomCheckViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor().newInstance()
    }
}