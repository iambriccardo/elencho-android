package com.riccardobusetti.unibztimetable.ui.launch

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.riccardobusetti.unibztimetable.R
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

        return getUserPrefsUseCase.getUserPrefs()
            .prefs.all { it.value.isNotBlank() && it.value.isNotEmpty() }
    }

    private fun launchMain() {
        launchActivity(MainActivity::class.java)
    }

    private fun launchConfiguration() {
        launchActivity(ConfigurationActivity::class.java)
    }

    private fun <T> launchActivity(clazz: Class<T>) {
        val intent = Intent(this, clazz)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        startActivity(intent)
    }
}
