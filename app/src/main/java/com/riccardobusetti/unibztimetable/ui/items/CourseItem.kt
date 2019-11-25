package com.riccardobusetti.unibztimetable.ui.items

import com.riccardobusetti.unibztimetable.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_course.view.*

class CourseItem(
    private val startTime: String,
    private val endTime: String,
    private val room: String,
    private val description: String,
    private val professor: String,
    private val type: String
) : Item<GroupieViewHolder>() {

    override fun getLayout() = R.layout.item_course

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            this.item_course_time.text = "$startTime - $endTime"
            this.item_course_room.text = room
            this.item_course_description.text = description
            this.item_course_professor.text = professor
            this.item_course_type.text = type
        }
    }
}
