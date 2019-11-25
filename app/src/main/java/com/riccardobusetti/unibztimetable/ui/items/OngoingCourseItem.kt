package com.riccardobusetti.unibztimetable.ui.items

import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.DisplayableCourse
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_ongoing_course.view.*

class OngoingCourseItem(
    private val displayableCourse: DisplayableCourse
) : Item<GroupieViewHolder>() {

    override fun getLayout() = R.layout.item_ongoing_course

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            this.item_ongoing_course_time.text =
                "${displayableCourse.startTime} - ${displayableCourse.endTime}"
            this.item_ongoing_course_room.text = displayableCourse.room
            this.item_ongoing_course_description.text = displayableCourse.description
            this.item_ongoing_course_professor.text = displayableCourse.professor
            this.item_ongoing_course_type.text = displayableCourse.type
        }
    }
}