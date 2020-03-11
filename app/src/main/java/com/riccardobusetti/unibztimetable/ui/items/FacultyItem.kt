package com.riccardobusetti.unibztimetable.ui.items

import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.choices.Degree
import com.riccardobusetti.unibztimetable.domain.entities.choices.Department
import com.riccardobusetti.unibztimetable.domain.entities.choices.FacultyChoice
import com.riccardobusetti.unibztimetable.domain.entities.choices.StudyPlan
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_faculty.view.*

class FacultyItem(
    private val facultyChoice: FacultyChoice,
    private val onClick: (FacultyChoice) -> Unit
) : Item<GroupieViewHolder>() {

    override fun getLayout() = R.layout.item_faculty

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            when (facultyChoice) {
                is Department -> {
                    this.item_faculty_main_text.text = facultyChoice.name
                }
                is Degree -> {
                    this.item_faculty_main_text.text = facultyChoice.name
                }
                is StudyPlan -> {
                    this.item_faculty_main_text.text = facultyChoice.year
                }
            }

            this.setOnClickListener {
                onClick(facultyChoice)
            }
        }
    }
}