package com.riccardobusetti.unibztimetable.ui.next7days

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.riccardobusetti.unibztimetable.domain.entities.Course
import com.riccardobusetti.unibztimetable.domain.entities.DisplayableCourseGroup
import com.riccardobusetti.unibztimetable.domain.entities.UserPrefs
import com.riccardobusetti.unibztimetable.domain.entities.app.AppSection
import com.riccardobusetti.unibztimetable.domain.entities.params.TimetableParams
import com.riccardobusetti.unibztimetable.domain.entities.safeGet
import com.riccardobusetti.unibztimetable.domain.usecases.GetNext7DaysTimetableUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.GetUserPrefsUseCase
import com.riccardobusetti.unibztimetable.ui.custom.TimetableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

class Next7DaysViewModel(
    private val context: Context,
    private val getNext7DaysTimetableUseCase: GetNext7DaysTimetableUseCase,
    private val getUserPrefsUseCase: GetUserPrefsUseCase
) : TimetableViewModel() {

    companion object {
        private const val TAG = "Next7DaysViewModel"
    }

    override fun start() {
        viewModelScope.safeLaunch(TAG) {
            for (request in timetableRequests) {
                hideError()
                showLoading(request.isReset)

                loadTimetable(getUserPrefs(), request.page)
                    .flowOn(Dispatchers.IO)
                    .onEach {
                        showTimetable(it, request.isReset)
                        hideLoading()
                    }
                    .handleErrors(TAG)
                    .collect()
            }
        }
    }

    private suspend fun getUserPrefs() = withContext(Dispatchers.IO) {
        getUserPrefsUseCase.execute(null)
    }

    private fun loadTimetable(
        userPrefs: UserPrefs,
        page: String
    ) = getNext7DaysTimetableUseCase.execute(
        TimetableParams(
            department = userPrefs.prefs.safeGet(UserPrefs.Pref.DEPARTMENT_ID),
            degree = userPrefs.prefs.safeGet(UserPrefs.Pref.DEGREE_ID),
            studyPlan = userPrefs.prefs.safeGet(UserPrefs.Pref.STUDY_PLAN_ID),
            page = page
        )
    )

    override fun coursesToCourseGroups(courses: List<Course>): List<DisplayableCourseGroup> {
        return DisplayableCourseGroup.build(courses, AppSection.NEXT_7_DAYS)
    }
}