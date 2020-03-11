package com.riccardobusetti.unibztimetable.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.riccardobusetti.unibztimetable.domain.entities.UserPrefs
import com.riccardobusetti.unibztimetable.domain.entities.params.TimetableParams
import com.riccardobusetti.unibztimetable.domain.entities.safeGet
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository
import com.riccardobusetti.unibztimetable.domain.repositories.UserPrefsRepository
import com.riccardobusetti.unibztimetable.domain.strategies.LocalTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.RemoteTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.SharedPreferencesUserPrefsStrategy
import com.riccardobusetti.unibztimetable.domain.usecases.GetUserPrefsUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.UpdateLocalTodayTimetableUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class TodayTimetableUpdateWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    companion object {
        const val TAG = "TodayTimetableWorker"
    }

    private val timetableRepository = TimetableRepository(
        LocalTimetableStrategy(context),
        RemoteTimetableStrategy()
    )

    private val getUserPrefsUseCase = GetUserPrefsUseCase(
        UserPrefsRepository(
            SharedPreferencesUserPrefsStrategy(context)
        )
    )

    private val updateLocalTodayTimetableUseCase = UpdateLocalTodayTimetableUseCase(
        timetableRepository
    )

    override suspend fun doWork(): Result = coroutineScope {
        try {
            withContext(Dispatchers.IO) {
                updateTodayTimetable(getUserPrefs())
            }
        } catch (e: Exception) {
            Log.d(TAG, "an error occurred while updating the today timetable: $e")
            Result.retry()
        }

        Result.success()
    }

    private suspend fun getUserPrefs() = withContext(Dispatchers.IO) {
        getUserPrefsUseCase.execute(null)
    }

    private fun updateTodayTimetable(
        userPrefs: UserPrefs
    ) = updateLocalTodayTimetableUseCase.execute(
        TimetableParams(
            department = userPrefs.prefs.safeGet(UserPrefs.Pref.DEPARTMENT_ID),
            degree = userPrefs.prefs.safeGet(UserPrefs.Pref.DEGREE_ID),
            studyPlan = userPrefs.prefs.safeGet(UserPrefs.Pref.STUDY_PLAN_ID),
            page = "0"
        )
    )
}