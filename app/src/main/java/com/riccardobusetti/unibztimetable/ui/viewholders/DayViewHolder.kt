package com.riccardobusetti.unibztimetable.ui.viewholders

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.riccardobusetti.unibztimetable.domain.entities.Day
import com.riccardobusetti.unibztimetable.ui.adapters.CourseAdapter
import kotlinx.android.synthetic.main.item_day.view.*

class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(context: Context, day: Day) {
        itemView.item_day_day.text = day.date

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        itemView.item_day_recycler_view.layoutManager = layoutManager

        val adapter = CourseAdapter(context)
        adapter.updateCourses(day.courses)

        itemView.item_day_recycler_view.adapter = adapter
    }
}