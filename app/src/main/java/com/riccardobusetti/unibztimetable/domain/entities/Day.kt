package com.riccardobusetti.unibztimetable.domain.entities

data class Day(
    val date: String,
    val courses: List<Course>,
    val description: String? = null,
    val isNow: Boolean? = null
)