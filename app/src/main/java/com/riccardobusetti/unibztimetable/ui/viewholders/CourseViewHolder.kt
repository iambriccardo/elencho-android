package com.riccardobusetti.unibztimetable.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.riccardobusetti.unibztimetable.domain.entities.Course
import kotlinx.android.synthetic.main.item_course.view.*

class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(course: Course) {
        itemView.item_course_location.text = course.location
        itemView.item_course_time.text = course.time
        itemView.item_course_title.text = course.title
        itemView.item_course_type.text = course.type
    }
}