package com.riccardobusetti.unibztimetable.ui.configuration

import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.UserPrefs
import com.riccardobusetti.unibztimetable.domain.repositories.UserPrefsRepository
import com.riccardobusetti.unibztimetable.domain.strategies.SharedPreferencesUserPrefsStrategy
import com.riccardobusetti.unibztimetable.domain.usecases.PutUserPrefsUseCase
import com.riccardobusetti.unibztimetable.ui.items.ConfigurationItem
import com.riccardobusetti.unibztimetable.ui.main.MainActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_configuration.*
import kotlinx.android.synthetic.main.bottom_sheet_url_listen.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ConfigurationActivity : AppCompatActivity() {

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private lateinit var model: ConfigurationViewModel

    private lateinit var bottomSheetView: View
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var recyclerView: RecyclerView
    private lateinit var saveButton: Button
    private lateinit var progressBar: ProgressBar

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
                showProgressBar()
            } else {
                hideProgressBar()
            }
        })

        model.success.observe(this, Observer { isSuccessful ->
            if (isSuccessful) {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                startActivity(intent)
            }
        })

        model.userPrefs.observe(this, Observer {
            if (it.size == UserPrefs.Pref.values().size) {
                showSaveButton()
            } else {
                hideSaveButton()
            }
        })
    }

    private fun setupUi() {
        bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_url_listen, null)
        bottomSheetView.bottom_sheet_url_listen_description.movementMethod =
            LinkMovementMethod.getInstance()

        bottomSheetDialog = BottomSheetDialog(this)
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

        progressBar = activity_configuration_progress_bar
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
                // TODO: change coroutine scope from Global to Activity specific.
                GlobalScope.launch(Dispatchers.Main) {
                    showUrlReading()

                    bottomSheetDialog.show()

                    delay(1000)

                    if (checkClipboardContent()) {
                        showUrlReadingSuccess()
                        successful(true)
                    } else {
                        showUrlReadingError()
                        successful(false)
                    }

                    delay(5000)

                    bottomSheetDialog.hide()
                }
            }
        }
    }

    private fun checkClipboardContent(): Boolean {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        val clipData = clipboard.primaryClip
        val clipDescription = clipboard.primaryClipDescription

        if (clipDescription!!.hasMimeType(MIMETYPE_TEXT_PLAIN)) {
            if (model.handleStudyPlanConfiguration("${clipData!!.getItemAt(0).text}")) {
                return true
            }
        }

        return false
    }

    private fun showSaveButton() {
        saveButton.visibility = View.VISIBLE
    }

    private fun hideSaveButton() {
        saveButton.visibility = View.GONE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun showUrlReading() {
        bottomSheetView.bottom_sheet_url_listen_title.text = getString(R.string.listen_url_title)
        bottomSheetView.bottom_sheet_url_listen_description.text =
            getString(R.string.listen_url_description)
        bottomSheetView.bottom_sheet_url_listen_progress_bar.visibility = View.VISIBLE
    }

    private fun showUrlReadingSuccess() {
        bottomSheetView.bottom_sheet_url_listen_title.text =
            getString(R.string.listen_url_description_success)
        bottomSheetView.bottom_sheet_url_listen_description.text =
            getString(R.string.listen_url_description_success)
        bottomSheetView.bottom_sheet_url_listen_progress_bar.visibility = View.GONE
    }

    private fun showUrlReadingError() {
        bottomSheetView.bottom_sheet_url_listen_title.text =
            getString(R.string.listen_url_title_error)
        bottomSheetView.bottom_sheet_url_listen_description.text =
            getString(R.string.listen_url_description_error)
        bottomSheetView.bottom_sheet_url_listen_progress_bar.visibility = View.GONE
    }
}
