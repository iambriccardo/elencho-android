package com.riccardobusetti.unibztimetable.ui.items

import android.view.View
import com.riccardobusetti.unibztimetable.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_day.view.*

class DayItem(
    private val description: String,
    private val isNow: Boolean = false
) : Item<GroupieViewHolder>() {

    override fun getLayout() = R.layout.item_day

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            this.item_day_date.text = description
            this.item_day_lottie_view.visibility = if (isNow) View.VISIBLE else View.GONE
        }
    }
}