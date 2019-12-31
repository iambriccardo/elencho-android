package com.riccardobusetti.unibztimetable.ui.launch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.onlyMandatory
import com.riccardobusetti.unibztimetable.domain.repositories.UserPrefsRepository
import com.riccardobusetti.unibztimetable.domain.strategies.SharedPreferencesUserPrefsStrategy
import com.riccardobusetti.unibztimetable.domain.usecases.GetUserPrefsUseCase
import com.riccardobusetti.unibztimetable.ui.configuration.ConfigurationActivity
import com.riccardobusetti.unibztimetable.ui.main.MainActivity

class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
    }

    override fun onResume() {
        super.onResume()

        checkLaunch()
        finish()
    }

    private fun checkLaunch() {
        if (isApplicationConfigured()) {
            launchMain()
        } else {
            launchConfiguration()
        }
    }

    private fun isApplicationConfigured(): Boolean {
        val getUserPrefsUseCase = GetUserPrefsUseCase(
            UserPrefsRepository(SharedPreferencesUserPrefsStrategy(this))
        )

        val userPrefs = getUserPrefsUseCase.getUserPrefs()
            .prefs

        Log.d("UserPrefs", "$userPrefs")

        return userPrefs.isNotEmpty() && userPrefs
            .onlyMandatory()
            .all { it.value.isNotBlank() && it.value.isNotEmpty() }
    }

    private fun launchMain() {
        Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    }

    private fun launchConfiguration() {
        Intent(this, ConfigurationActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(ConfigurationActivity.IS_FIRST_CONFIGURATION_KEY, true)
            startActivity(this)
        }
    }
}
