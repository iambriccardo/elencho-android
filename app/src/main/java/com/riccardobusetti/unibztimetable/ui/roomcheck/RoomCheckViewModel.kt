package com.riccardobusetti.unibztimetable.ui.roomcheck

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.riccardobusetti.unibztimetable.data.remote.retrofit.BackendRetrofitClient
import com.riccardobusetti.unibztimetable.domain.entities.availability.RoomAvailability
import com.riccardobusetti.unibztimetable.ui.custom.BaseViewModel
import com.riccardobusetti.unibztimetable.utils.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomCheckViewModel: BaseViewModel() {

    companion object {
        private const val TAG = "RoomCheckViewModel"
    }

    private val _availability = MutableLiveData<RoomAvailability>()
    val availability: LiveData<RoomAvailability>
        get() = _availability

    private val _status = MutableLiveData<RoomCheckFragment.CheckStatus>(RoomCheckFragment.CheckStatus.SEARCH)
    val status: LiveData<RoomCheckFragment.CheckStatus>
        get() = _status

    override fun start() {}

    fun checkRoomAvailability(room: String) {
        viewModelScope.safeLaunch(TAG, {
            _status.value = RoomCheckFragment.CheckStatus.LOADING

            val result = withContext(Dispatchers.IO) {
                BackendRetrofitClient.webservice.checkAvailability(room, DateUtils.getCurrentTimeFormatted())
            }

            _availability.value = result

            when {
                result.isDayEmpty -> {
                    _status.value = RoomCheckFragment.CheckStatus.LOADED_WITH_FULL_DAY_AVAILABLE
                }
                result.availabilities.isEmpty() -> {
                    _status.value = RoomCheckFragment.CheckStatus.LOADED_WITH_NO_AVAILABILITIES
                }
                else -> {
                    _status.value = RoomCheckFragment.CheckStatus.LOADED_WITH_AVAILABILITIES
                }
            }
        }, {
            _status.value = RoomCheckFragment.CheckStatus.ERROR
        })
    }
}