package com.riccardobusetti.unibztimetable.ui.choosefaculty

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.riccardobusetti.unibztimetable.domain.entities.UserPrefs
import com.riccardobusetti.unibztimetable.domain.entities.choices.Degree
import com.riccardobusetti.unibztimetable.domain.entities.choices.Department
import com.riccardobusetti.unibztimetable.domain.entities.choices.FacultyChoice
import com.riccardobusetti.unibztimetable.domain.entities.choices.StudyPlan
import com.riccardobusetti.unibztimetable.domain.entities.params.UserPrefsParams
import com.riccardobusetti.unibztimetable.domain.repositories.ChooseFacultyRepository
import com.riccardobusetti.unibztimetable.domain.usecases.PutUserPrefsUseCase
import com.riccardobusetti.unibztimetable.ui.custom.AdvancedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChooseFacultyViewModel(
    private val context: Context,
    private val repository: ChooseFacultyRepository,
    private val putUserPrefsUseCase: PutUserPrefsUseCase
) : AdvancedViewModel() {

    companion object {
        private const val TAG = "ChooseFacultyViewModel"
    }

    private val _choices = MutableLiveData<List<FacultyChoice>>()
    val choices: LiveData<List<FacultyChoice>>
        get() = _choices

    private val _showNextButton = MutableLiveData<Boolean>(false)
    val showNextButton: LiveData<Boolean>
        get() = _showNextButton

    private var selectedDepartment: Department? = null
    private var selectedDegree: Degree? = null
    private var selectedStudyPlan: StudyPlan? = null

    override fun start() {
        loadDepartments()
    }

    private fun loadDepartments() {
        viewModelScope.safeLaunch(TAG) {
            _choices.value = withContext(Dispatchers.IO) {
                repository.getDepartments()
            }
        }
    }

    private fun loadDegrees(departmentId: String? = null) {
        viewModelScope.safeLaunch(TAG) {
            _choices.value = withContext(Dispatchers.IO) {
                if (departmentId != null) {
                    repository.getDegrees(departmentId)
                } else {
                    repository.getDegrees()
                }
            }

            if (choices.value!!.isEmpty()) {
                showNextButton()
            }
        }
    }

    private fun loadStudyPlans(degreeId: String? = null) {
        viewModelScope.safeLaunch(TAG) {
            _choices.value = withContext(Dispatchers.IO) {
                if (degreeId != null) {
                    repository.getStudyPlans(degreeId)
                } else {
                    repository.getStudyPlans()
                }
            }

            if (choices.value!!.isEmpty()) {
                showNextButton()
            }
        }
    }

    private fun showNextButton() {
        _showNextButton.value = true
    }

    fun selectDepartment(department: Department) {
        selectedDepartment = department
        loadDegrees(department.id)
    }

    fun selectDegree(degree: Degree) {
        selectedDegree = degree
        loadStudyPlans(degree.id)
    }

    fun selectStudyPlan(studyPlan: StudyPlan) {
        selectedStudyPlan = studyPlan
        showNextButton()
    }

    fun saveUserPrefs() {
        viewModelScope.safeLaunch(TAG) {
            withContext(Dispatchers.IO) {
                putUserPrefsUseCase.execute(UserPrefsParams(buildUserPrefs()))
            }
        }
    }

    private fun buildUserPrefs(): UserPrefs {
        val userPrefsMap = mutableMapOf<UserPrefs.Pref, String>()

        userPrefsMap[UserPrefs.Pref.DEPARTMENT_KEY] =
            if (selectedDepartment != null) selectedDepartment!!.key else UserPrefs.NO_VALUE

        userPrefsMap[UserPrefs.Pref.DEGREE_KEY] =
            if (selectedDegree != null) selectedDegree!!.key else UserPrefs.NO_VALUE

        userPrefsMap[UserPrefs.Pref.STUDY_PLAN_KEY] =
            if (selectedStudyPlan != null) selectedStudyPlan!!.key else UserPrefs.NO_VALUE

        return UserPrefs(userPrefsMap)
    }
}