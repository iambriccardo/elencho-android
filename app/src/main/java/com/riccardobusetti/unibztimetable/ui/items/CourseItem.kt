package com.riccardobusetti.unibztimetable.ui.items

import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.Course
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_course.view.*

class CourseItem(
    private val course: Course
) : Item<GroupieViewHolder>() {

    override fun getLayout() = R.layout.item_course

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            this.item_course_location.text = course.location
            this.item_course_location.isSelected = true
            this.item_course_time.text = course.time
            this.item_course_time.isSelected = true
            this.item_course_title.text = course.title
            this.item_course_professor.text = course.professor
            this.item_course_professor.isSelected = true
            this.item_course_type.text = course.type
            this.item_course_type.isSelected = true
        }
    }
}
