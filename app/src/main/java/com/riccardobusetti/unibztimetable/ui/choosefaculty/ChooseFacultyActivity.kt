package com.riccardobusetti.unibztimetable.ui.choosefaculty

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.Degree
import com.riccardobusetti.unibztimetable.domain.entities.Department
import com.riccardobusetti.unibztimetable.domain.entities.StudyPlan
import com.riccardobusetti.unibztimetable.domain.repositories.ChooseFacultyRepository
import kotlinx.android.synthetic.main.activity_choose_faculty.*

class ChooseFacultyActivity : AppCompatActivity() {

    private val model: ChooseFacultyViewModel by viewModels {
        ChooseFacultyViewModelFactory(this, ChooseFacultyRepository())
    }

    private lateinit var departmentsAdapter: ArrayAdapter<Department>
    private lateinit var degreesAdapter: ArrayAdapter<Degree>
    private lateinit var studyPlansAdapter: ArrayAdapter<StudyPlan>

    private lateinit var departmentSpinner: Spinner
    private lateinit var degreeSpinner: Spinner
    private lateinit var studyPlanSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_faculty)

        setUpUI()
        attachObservers()
    }

    private fun setUpUI() {
        departmentSpinner = activity_choose_faculty_department_spinner
        departmentsAdapter = ArrayAdapter(this, R.layout.item_spinner, mutableListOf())
        departmentSpinner.adapter = departmentsAdapter

        degreeSpinner = activity_choose_faculty_degree_spinner
        degreesAdapter = ArrayAdapter(this, R.layout.item_spinner, mutableListOf())
        degreeSpinner.adapter = degreesAdapter

        studyPlanSpinner = activity_choose_faculty_study_plan_spinner
        studyPlansAdapter = ArrayAdapter(this, R.layout.item_spinner, mutableListOf())
        studyPlanSpinner.adapter = studyPlansAdapter
    }

    private fun attachObservers() {
        model.apply {
            departments.observe(this@ChooseFacultyActivity, Observer { departments ->
                departmentsAdapter.clear()
                departmentsAdapter.addAll(departments)
            })

            degrees.observe(this@ChooseFacultyActivity, Observer { degrees ->
                degreesAdapter.clear()
                degreesAdapter.addAll(degrees)
            })

            studyPlans.observe(this@ChooseFacultyActivity, Observer { studyPlans ->
                studyPlansAdapter.clear()
                studyPlansAdapter.addAll(studyPlans)
            })
        }
    }
}