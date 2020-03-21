package com.riccardobusetti.unibztimetable.domain.entities.availability

data class RoomAvailability(
    val room: String,
    val isDayEmpty: Boolean,
    val availabilities: List<Availability>
)