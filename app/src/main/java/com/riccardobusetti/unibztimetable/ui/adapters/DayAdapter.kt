package com.riccardobusetti.unibztimetable.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.riccardobusetti.unibztimetable.domain.entities.Day
import com.riccardobusetti.unibztimetable.ui.viewholders.DayViewHolder
import com.riccardobusetti.unibztimetable.R

class DayAdapter(private val context: Context) : RecyclerView.Adapter<DayViewHolder>() {

    private var days = emptyList<Day>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DayViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_day,
                parent,
                false
            )
        )

    override fun getItemCount() = days.size


    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(context, days[position])
    }

    fun updateDays(newDays: List<Day>) {
        days = newDays
        notifyDataSetChanged()
    }
}