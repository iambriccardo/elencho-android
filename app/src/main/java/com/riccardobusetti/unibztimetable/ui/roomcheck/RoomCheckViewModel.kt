package com.riccardobusetti.unibztimetable.ui.roomcheck

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riccardobusetti.unibztimetable.domain.entities.AppSection
import com.riccardobusetti.unibztimetable.domain.entities.DisplayableCourseGroup
import com.riccardobusetti.unibztimetable.domain.usecases.CheckRoomAvailabilityUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class RoomCheckViewModel(
    private val checkRoomAvailabilityUseCase: CheckRoomAvailabilityUseCase
) : ViewModel() {

    private val _nextRoomTimetable = MutableLiveData<DisplayableCourseGroup>()
    val nextRoomTimetable: LiveData<DisplayableCourseGroup>
        get() = _nextRoomTimetable

    fun checkRoomAvailability(room: String) {
        viewModelScope.launch {
            checkRoomAvailabilityUseCase.checkTodaysRoomAvailability(room)
                .onEach {
                    _nextRoomTimetable.value =
                        DisplayableCourseGroup.build(it, AppSection.ROOM_CHECK).first()
                }
                .collect()
        }
    }
}