package com.riccardobusetti.unibztimetable.ui.next7days

import androidx.lifecycle.viewModelScope
import com.riccardobusetti.unibztimetable.domain.entities.UserPrefs
import com.riccardobusetti.unibztimetable.domain.entities.safeGet
import com.riccardobusetti.unibztimetable.domain.usecases.GetNext7DaysTimetableUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.GetUserPrefsUseCase
import com.riccardobusetti.unibztimetable.utils.custom.TimetableViewModel
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

    fun loadNext7DaysTimetable(page: String = DEFAULT_PAGE) {
        viewModelScope.launchWithSupervisor {
            hideError()
            showLoading()

            loadTimetable(getUserPrefs(), page)
                .flowOn(Dispatchers.IO)
                .onEach {
                    hideLoading()
                    showTimetable(it)
                }
                .handleErrors(TAG)
                .collect()
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
}