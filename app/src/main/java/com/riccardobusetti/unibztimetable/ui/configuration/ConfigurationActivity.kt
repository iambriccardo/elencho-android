package com.riccardobusetti.unibztimetable.ui.configuration

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.timePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.data.remote.WebSiteUrl
import com.riccardobusetti.unibztimetable.domain.entities.UserPrefs
import com.riccardobusetti.unibztimetable.domain.entities.onlyMandatory
import com.riccardobusetti.unibztimetable.domain.repositories.UserPrefsRepository
import com.riccardobusetti.unibztimetable.domain.strategies.SharedPreferencesUserPrefsStrategy
import com.riccardobusetti.unibztimetable.domain.usecases.PutUserPrefsUseCase
import com.riccardobusetti.unibztimetable.ui.items.ConfigurationItem
import com.riccardobusetti.unibztimetable.ui.main.MainActivity
import com.riccardobusetti.unibztimetable.utils.DateUtils
import com.riccardobusetti.unibztimetable.utils.ScreenUtils
import com.riccardobusetti.unibztimetable.utils.custom.StrictWebViewClient
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_configuration.*
import kotlinx.android.synthetic.main.bottom_sheet_timetable_web_view.view.*


class ConfigurationActivity : AppCompatActivity() {

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private lateinit var model: ConfigurationViewModel

    private lateinit var bottomSheetView: View
    private lateinit var webView: WebView
    private lateinit var webViewProgressBar: ProgressBar
    private lateinit var copyButton: Button
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var recyclerView: RecyclerView
    private lateinit var saveButton: Button
    private lateinit var configurationProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration)

        initModel()
        attachObservers()
        setupUi()
        loadConfigurations()
    }

    private fun initModel() {
        val userPrefsRepository = UserPrefsRepository(SharedPreferencesUserPrefsStrategy(this))

        model = ViewModelProviders.of(
            this,
            ConfigurationViewModelFactory(
                this,
                PutUserPrefsUseCase(userPrefsRepository)
            )
        )
            .get(
                ConfigurationViewModel::class.java
            )
    }

    private fun attachObservers() {
        model.loading.observe(this, Observer { isLoading ->
            if (isLoading) {
                hideSaveButton()
                showConfigurationLoading()
            } else {
                hideConfigurationLoading()
            }
        })

        model.success.observe(this, Observer { isSuccessful ->
            if (isSuccessful) {
                // TODO: check [Activity com.riccardobusetti.unibztimetable.ui.configuration.ConfigurationActivity has leaked window DecorView@f9ce725[ConfigurationActivity] that was originally added here]
                Intent(this, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(this)
                    finish()
                }
            }
        })

        model.userPrefs.observe(this, Observer { userPrefs ->
            if (userPrefs.onlyMandatory().size == UserPrefs.Pref.values().onlyMandatory().size) {
                showSaveButton()
            } else {
                hideSaveButton()
            }
        })
    }

    private fun setupUi() {
        bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_timetable_web_view, null)

        webView = bottomSheetView.bottom_sheet_timetable_web_view_web_view
        applyWebViewSettings(webView)
        webView.loadUrl(WebSiteUrl.BASE_TIMETABLE_URL)

        webViewProgressBar = bottomSheetView.bottom_sheet_timetable_web_view_progress

        copyButton = bottomSheetView.bottom_sheet_timetable_web_view_copy_link

        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.behavior.peekHeight = ScreenUtils.getScreenHeight(this)
        bottomSheetDialog.behavior.isHideable = false
        bottomSheetDialog.setContentView(bottomSheetView)

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

    @SuppressLint("SetJavaScriptEnabled")
    private fun applyWebViewSettings(webView: WebView) {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.overScrollMode = WebView.OVER_SCROLL_NEVER

        val client = StrictWebViewClient(this, listOf(WebSiteUrl.TIMETABLE_URL_REGEX))
        client.listenForProgress({
            showWebViewLoading()
        }, {
            hideWebViewLoading()
        })

        webView.webViewClient = client
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
        Toast.makeText(this, R.string.copy_link_tutorial, Toast.LENGTH_LONG).show()
        bottomSheetDialog.show()

        copyButton.setOnClickListener {
            val result = model.handleStudyPlanConfiguration(webView.url)
            successful(result)

            if (result) {
                bottomSheetDialog.hide()
            } else {
                Toast.makeText(this, R.string.error_while_reading_link, Toast.LENGTH_SHORT)
                    .show()
            }

            copyButton.setOnClickListener(null)
        }
    }

    private fun dailyNotification(successful: (Boolean) -> Unit) {
        MaterialDialog(this).show {
            timePicker(currentTime = DateUtils.getCurrentCalendar()) { _, datetime ->
                successful(model.handleDailyNotificationConfiguration(datetime))
            }
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

    private fun showWebViewLoading() {
        webViewProgressBar.visibility = View.VISIBLE
        copyButton.setText(R.string.website_loading)
        copyButton.isEnabled = false
    }

    private fun hideWebViewLoading() {
        webViewProgressBar.visibility = View.GONE
        copyButton.setText(R.string.copy_link)
        copyButton.isEnabled = true
    }
}
