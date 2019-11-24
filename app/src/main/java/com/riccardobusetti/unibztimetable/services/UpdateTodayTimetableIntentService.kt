package com.riccardobusetti.unibztimetable.services

import android.app.IntentService
import android.content.Intent
import com.riccardobusetti.unibztimetable.domain.entities.UserPrefs
import com.riccardobusetti.unibztimetable.domain.entities.safeGet
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository
import com.riccardobusetti.unibztimetable.domain.repositories.UserPrefsRepository
import com.riccardobusetti.unibztimetable.domain.strategies.LocalTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.RemoteTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.SharedPreferencesUserPrefsStrategy
import com.riccardobusetti.unibztimetable.domain.usecases.GetUserPrefsUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.UpdateLocalTodayTimetableUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class UpdateTodayTimetableIntentService :
    IntentService(UpdateTodayTimetableIntentService::class.java.simpleName) {

    private val getUserPrefsUseCase = GetUserPrefsUseCase(
        UserPrefsRepository(
            SharedPreferencesUserPrefsStrategy(this)
        )
    )

    private val updateLocalTodayTimetableUseCase = UpdateLocalTodayTimetableUseCase(
        TimetableRepository(
            LocalTimetableStrategy(this),
            RemoteTimetableStrategy()
        )
    )

    override fun onHandleIntent(p0: Intent?) {
        runBlocking {
            withContext(Dispatchers.IO) {
                // TODO: handle errors, internet connectivity absence and edge cases.
                updateTodayTimetable(getUserPrefs())
            }
        }

        val intent = Intent(this, ShowTodayTimetableIntentService::class.java)
        startService(intent)
    }

    private suspend fun getUserPrefs() = withContext(Dispatchers.IO) {
        getUserPrefsUseCase.getUserPrefs()
    }

    private fun updateTodayTimetable(
        userPrefs: UserPrefs
    ) = updateLocalTodayTimetableUseCase.updateLocalTodayTimetable(
        userPrefs.prefs.safeGet(UserPrefs.Pref.DEPARTMENT_ID),
        userPrefs.prefs.safeGet(UserPrefs.Pref.DEGREE_ID),
        userPrefs.prefs.safeGet(UserPrefs.Pref.STUDY_PLAN_ID),
        "0"
    )
}