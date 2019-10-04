package com.riccardobusetti.unibztimetable.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.Course
import com.riccardobusetti.unibztimetable.ui.viewholders.CourseViewHolder

class CourseAdapter(private val context: Context) : RecyclerView.Adapter<CourseViewHolder>() {

    private var courses = emptyList<Course>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CourseViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_course, parent, false)
        )

    override fun getItemCount() = courses.size

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(courses[position])
    }

    fun updateCourses(newCourses: List<Course>) {
        courses = newCourses
        notifyDataSetChanged()
    }
}