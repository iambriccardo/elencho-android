package com.riccardobusetti.unibztimetable.ui.roomcheck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.AppSection
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository
import com.riccardobusetti.unibztimetable.domain.strategies.LocalTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.RemoteTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.usecases.CheckRoomAvailabilityUseCase
import com.riccardobusetti.unibztimetable.ui.custom.BaseFragment


class RoomCheckFragment : BaseFragment<RoomCheckViewModel>() {

    override val appSection: AppSection
        get() = AppSection.ROOM_CHECK

    override fun initViewModel(): RoomCheckViewModel {
        val timetableRepository =
            TimetableRepository(
                LocalTimetableStrategy(requireContext()),
                RemoteTimetableStrategy()
            )

        return ViewModelProviders.of(
            this,
            RoomCheckViewModelFactory(
                CheckRoomAvailabilityUseCase(timetableRepository)
            )
        ).get(RoomCheckViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_room_check, container, false)
    }

    override fun setupUI() {

    }

    override fun attachObservers() {

    }
}