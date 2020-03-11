package com.riccardobusetti.unibztimetable.ui.choosefaculty

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.riccardobusetti.unibztimetable.domain.entities.Degree
import com.riccardobusetti.unibztimetable.domain.entities.Department
import com.riccardobusetti.unibztimetable.domain.entities.StudyPlan
import com.riccardobusetti.unibztimetable.domain.repositories.ChooseFacultyRepository
import com.riccardobusetti.unibztimetable.ui.custom.AdvancedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChooseFacultyViewModel(
    private val context: Context,
    private val repository: ChooseFacultyRepository
) : AdvancedViewModel() {

    companion object {
        private const val TAG = "ChooseFacultyViewModel"
    }

    private val _departments = MutableLiveData<List<Department>>()
    val departments: LiveData<List<Department>>
        get() = _departments

    private val _degrees = MutableLiveData<List<Degree>>()
    val degrees: LiveData<List<Degree>>
        get() = _degrees

    private val _studyPlans = MutableLiveData<List<StudyPlan>>()
    val studyPlans: LiveData<List<StudyPlan>>
        get() = _studyPlans

    init {
        start()
    }

    override fun start() {
        viewModelScope.safeLaunch(TAG) {
            val departments = withContext(Dispatchers.IO) {
                repository.getDepartments()
            }
            _departments.value = departments
        }
    }

    fun selectDepartment(department: Department) {

    }

    fun selectDegree(degree: Degree) {

    }

    fun selectStudyPlan(studyPlan: StudyPlan) {

    }
}