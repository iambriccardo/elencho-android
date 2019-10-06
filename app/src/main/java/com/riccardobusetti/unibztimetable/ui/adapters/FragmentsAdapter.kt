package com.riccardobusetti.unibztimetable.ui.adapters

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.riccardobusetti.unibztimetable.ui.main.MainActivity

/**
 * Adapter that will be used by the view pager to display the fragments.
 *
 * @author Riccardo Busetti
 */
class FragmentsAdapter(
    private val indexableFragments: List<MainActivity.IndexableFragment>,
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int) =
        indexableFragments.find { it.index == position }!!.fragment

    override fun getCount() = indexableFragments.size
}