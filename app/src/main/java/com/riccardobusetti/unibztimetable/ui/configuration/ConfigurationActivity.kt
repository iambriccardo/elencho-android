package com.riccardobusetti.unibztimetable.ui.configuration

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.timePicker
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.UserPrefs
import com.riccardobusetti.unibztimetable.domain.entities.onlyMandatory
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository
import com.riccardobusetti.unibztimetable.domain.repositories.UserPrefsRepository
import com.riccardobusetti.unibztimetable.domain.strategies.LocalTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.RemoteTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.SharedPreferencesUserPrefsStrategy
import com.riccardobusetti.unibztimetable.domain.usecases.DeleteLocalTimetableUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.PutUserPrefsUseCase
import com.riccardobusetti.unibztimetable.ui.choosefaculty.ChooseFacultyActivity
import com.riccardobusetti.unibztimetable.ui.items.ConfigurationItem
import com.riccardobusetti.unibztimetable.ui.main.MainActivity
import com.riccardobusetti.unibztimetable.utils.DateUtils
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_configuration.*


class ConfigurationActivity : AppCompatActivity() {

    companion object {
        const val IS_FIRST_CONFIGURATION_KEY = "IS_FIRST_CONFIGURATION"
    }

    private var isFirstConfiguration = true

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private val model: ConfigurationViewModel by viewModels {
        val userPrefsRepository = UserPrefsRepository(SharedPreferencesUserPrefsStrategy(this))
        val timetableRepository = TimetableRepository(
            LocalTimetableStrategy(this),
            RemoteTimetableStrategy()
        )

        ConfigurationViewModelFactory(
            this,
            PutUserPrefsUseCase(userPrefsRepository),
            DeleteLocalTimetableUseCase(timetableRepository)
        )
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var saveButton: Button
    private lateinit var configurationProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration)

        intent.extras?.let {
            isFirstConfiguration = it.getBoolean(IS_FIRST_CONFIGURATION_KEY)
        }

        setupUi()
        attachObservers()
        loadConfigurations()
    }

    private fun setupUi() {
        recyclerView = activity_configuration_recycler_view
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ConfigurationActivity)
            adapter = groupAdapter
        }

        saveButton = activity_configuration_save_button
        saveButton.setOnClickListener {
            model.putUserPrefs()
        }

        configurationProgressBar = activity_configuration_progress_bar
    }

    private fun attachObservers() {
        model.apply {
            loading.observe(this@ConfigurationActivity, Observer { isLoading ->
                if (isLoading) {
                    hideSaveButton()
                    showConfigurationLoading()
                } else {
                    hideConfigurationLoading()
                }
            })

            success.observe(this@ConfigurationActivity, Observer { isSuccessful ->
                if (isSuccessful) {
                    // TODO: check [Activity com.riccardobusetti.unibztimetable.ui.configuration.ConfigurationActivity has leaked window DecorView@f9ce725[ConfigurationActivity] that was originally added here]
                    Intent(this@ConfigurationActivity, MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(this)
                        finish()
                    }
                }
            })

            userPrefs.observe(this@ConfigurationActivity, Observer { userPrefs ->
                if (isConfigured(userPrefs)) {
                    showSaveButton()
                } else {
                    hideSaveButton()
                }
            })
        }
    }

    private fun loadConfigurations() {
        ConfigurationViewModel.Configuration.values().forEach {
            groupAdapter.add(ConfigurationItem(this, it) { configuration, successful ->
                handleConfigurationClick(configuration, successful)
            })
        }
    }

    private fun handleConfigurationClick(
        configuration: ConfigurationViewModel.Configuration,
        successful: (Boolean) -> Unit
    ) {
        when (configuration) {
            ConfigurationViewModel.Configuration.STUDY_PLAN -> {
                studyPlan(successful)
            }
            ConfigurationViewModel.Configuration.DAILY_NOTIFICATION -> {
                dailyNotification(successful)
            }
        }
    }

    private fun studyPlan(successful: (Boolean) -> Unit) {
        Intent(this, ChooseFacultyActivity::class.java).apply {
            startActivity(this)
        }
    }

    private fun dailyNotification(successful: (Boolean) -> Unit) {
        MaterialDialog(this).show {
            timePicker(currentTime = DateUtils.getCurrentCalendar()) { _, datetime ->
                successful(model.handleDailyNotificationConfiguration(datetime))
            }
        }
    }

    private fun isConfigured(userPrefs: Map<UserPrefs.Pref, String>): Boolean {
        return if (isFirstConfiguration) {
            userPrefs.onlyMandatory().size == UserPrefs.Pref.values().onlyMandatory().size
        } else {
            userPrefs.isNotEmpty()
        }
    }


    private fun showSaveButton() {
        saveButton.visibility = View.VISIBLE
    }

    private fun hideSaveButton() {
        saveButton.visibility = View.GONE
    }

    private fun showConfigurationLoading() {
        configurationProgressBar.visibility = View.VISIBLE
    }

    private fun hideConfigurationLoading() {
        configurationProgressBar.visibility = View.GONE
    }
}
