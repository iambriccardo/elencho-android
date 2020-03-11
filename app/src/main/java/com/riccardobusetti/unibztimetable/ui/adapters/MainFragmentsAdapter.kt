package com.riccardobusetti.unibztimetable.ui.adapters

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.riccardobusetti.unibztimetable.domain.entities.app.AppFragment

/**
 * Adapter that will be used by the view pager to display the main fragments.
 *
 * @author Riccardo Busetti
 */
class MainFragmentsAdapter(
    private val mainFragments: List<AppFragment>,
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int) =
        mainFragments.find { it.index == position }!!.fragment

    override fun getCount() = mainFragments.size
}