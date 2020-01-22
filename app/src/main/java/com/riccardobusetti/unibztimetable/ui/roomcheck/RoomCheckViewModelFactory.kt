package com.riccardobusetti.unibztimetable.ui.roomcheck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riccardobusetti.unibztimetable.domain.usecases.CheckRoomAvailabilityUseCase

class RoomCheckViewModelFactory(
    private val checkRoomAvailabilityUseCase: CheckRoomAvailabilityUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            CheckRoomAvailabilityUseCase::class.java
        )
            .newInstance(checkRoomAvailabilityUseCase)
    }
}