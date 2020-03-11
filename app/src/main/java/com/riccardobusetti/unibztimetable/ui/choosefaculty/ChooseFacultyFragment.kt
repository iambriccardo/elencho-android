package com.riccardobusetti.unibztimetable.ui.choosefaculty

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.Degree
import com.riccardobusetti.unibztimetable.domain.entities.Department
import com.riccardobusetti.unibztimetable.domain.entities.StudyPlan
import com.riccardobusetti.unibztimetable.domain.entities.app.AppSection
import com.riccardobusetti.unibztimetable.domain.repositories.ChooseFacultyRepository
import com.riccardobusetti.unibztimetable.ui.custom.BaseFragment
import kotlinx.android.synthetic.main.fragment_choose_faculty.*

class ChooseFacultyFragment : BaseFragment<ChooseFacultyViewModel>() {

    override val appSection: AppSection
        get() = AppSection.CHOOSE_FACULTY

    private lateinit var departmentsAdapter: ArrayAdapter<Department>
    private lateinit var degreesAdapter: ArrayAdapter<Degree>
    private lateinit var studyPlansAdapter: ArrayAdapter<StudyPlan>

    private lateinit var departmentSpinner: Spinner
    private lateinit var degreeSpinner: Spinner
    private lateinit var studyPlanSpinner: Spinner

    override fun initViewModel(): ChooseFacultyViewModel {
        return ViewModelProviders.of(
            this,
            ChooseFacultyViewModelFactory(requireContext(), ChooseFacultyRepository())
        ).get(ChooseFacultyViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_choose_faculty, container, false)
    }

    override fun setupUI() {
        departmentSpinner = fragment_choose_faculty_department_spinner
        departmentsAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner, mutableListOf())
        departmentSpinner.adapter = departmentsAdapter

        degreeSpinner = fragment_choose_faculty_degree_spinner
        degreesAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner, mutableListOf())
        degreeSpinner.adapter = degreesAdapter

        studyPlanSpinner = fragment_choose_faculty_study_plan_spinner
        studyPlansAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner, mutableListOf())
        studyPlanSpinner.adapter = studyPlansAdapter
    }

    override fun attachObservers() {
        model?.let {
            it.departments.observe(this@ChooseFacultyFragment, Observer { departments ->
                departmentsAdapter.clear()
                departmentsAdapter.addAll(departments)
            })

            it.degrees.observe(this@ChooseFacultyFragment, Observer { degrees ->
                degreesAdapter.clear()
                degreesAdapter.addAll(degrees)
            })

            it.studyPlans.observe(this@ChooseFacultyFragment, Observer { studyPlans ->
                studyPlansAdapter.clear()
                studyPlansAdapter.addAll(studyPlans)
            })
        }
    }
}