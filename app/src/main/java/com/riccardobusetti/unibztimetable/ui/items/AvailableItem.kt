package com.riccardobusetti.unibztimetable.ui.items

import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.availability.Availability
import com.riccardobusetti.unibztimetable.utils.DateUtils
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_available.view.*

class AvailableItem (
    private val availability: Availability
) : Item<GroupieViewHolder>() {

    override fun getLayout() = R.layout.item_available

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            this.item_available_time.text = computeText()
        }
    }

    private fun computeText(): String {
        val from = if (availability.from != null) {
            formatDate(availability.from)
        } else {
            "University opening"
        }

        val to = if (availability.to != null) {
            formatDate(availability.to)
        } else {
            "University closing"
        }

        return "$from - $to"
    }

    private fun formatDate(date: String): String {
        return DateUtils.formatLocalDateTime(
            DateUtils.parseLocalDateTime(date, DateUtils.BACKEND_DATE_TIME_FORMAT,true),
            "HH:mm",
            true
        )
    }
}
