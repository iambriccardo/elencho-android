package com.riccardobusetti.unibztimetable.ui.adapters

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.riccardobusetti.unibztimetable.domain.entities.app.AppFragment

/**
 * Adapter that will be used by the view pager to display the setup fragments.
 *
 * @author Riccardo Busetti
 */
class SetupFragmentsAdapter(
    private val setupFragments: List<AppFragment>,
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int) =
        setupFragments.find { it.index == position }!!.fragment

    override fun getCount() = setupFragments.size
}