package com.riccardobusetti.unibztimetable.domain.entities.app

import androidx.fragment.app.Fragment

data class AppFragment(
    val index: Int,
    val itemId: Int? = null,
    val fragment: Fragment
)