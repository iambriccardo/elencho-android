package com.riccardobusetti.unibztimetable.ui.items

import android.view.View
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.Day
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_day.view.*

class DayItem(
    private val day: Day
) : Item<GroupieViewHolder>() {

    override fun getLayout() = R.layout.item_day

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            this.item_day_date.text = day.description ?: day.date
            day.isNow?.let {
                this.item_day_lottie_view.visibility = if (it) View.VISIBLE else View.GONE
            }
        }
    }
}