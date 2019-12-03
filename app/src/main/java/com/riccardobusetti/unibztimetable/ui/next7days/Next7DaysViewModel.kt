package com.riccardobusetti.unibztimetable.ui.next7days

import androidx.lifecycle.viewModelScope
import com.riccardobusetti.unibztimetable.domain.entities.*
import com.riccardobusetti.unibztimetable.domain.usecases.GetNext7DaysTimetableUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.GetUserPrefsUseCase
import com.riccardobusetti.unibztimetable.ui.custom.TimetableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

class Next7DaysViewModel(
    private val getNext7DaysTimetableUseCase: GetNext7DaysTimetableUseCase,
    private val getUserPrefsUseCase: GetUserPrefsUseCase
) : TimetableViewModel() {

    companion object {
        private const val TAG = "Next7DaysViewModel"
    }

    init {
        start()
    }

    override fun start() {
        viewModelScope.launchWithSupervisor {
            for (request in timetableRequests) {
                hideError()
                showLoading()

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
        getUserPrefsUseCase.getUserPrefs()
    }

    private fun loadTimetable(
        userPrefs: UserPrefs,
        page: String
    ) = getNext7DaysTimetableUseCase.getNext7DaysTimetable(
        userPrefs.prefs.safeGet(UserPrefs.Pref.DEPARTMENT_ID),
        userPrefs.prefs.safeGet(UserPrefs.Pref.DEGREE_ID),
        userPrefs.prefs.safeGet(UserPrefs.Pref.STUDY_PLAN_ID),
        page
    )

    override fun coursesToCourseGroups(courses: List<Course>): List<DisplayableCourseGroup> {
        return DisplayableCourseGroup.build(courses, AppSection.NEXT_7_DAYS)
    }
}