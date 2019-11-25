package com.riccardobusetti.unibztimetable.ui.items

import android.view.View
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.DisplayableCourseGroup
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_course_group.view.*

class CourseGroupItem(
    private val displayableCourseGroup: DisplayableCourseGroup
) : Item<GroupieViewHolder>() {

    override fun getLayout() = R.layout.item_course_group

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            this.item_course_group_title.text = displayableCourseGroup.title
            this.item_course_group_lottie_view.visibility =
                if (displayableCourseGroup.isNow) View.VISIBLE else View.GONE
        }
    }
}