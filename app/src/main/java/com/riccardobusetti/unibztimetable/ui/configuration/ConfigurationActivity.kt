package com.riccardobusetti.unibztimetable.ui.configuration

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

class ConfigurationActivity : AppCompatActivity() {

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private lateinit var model: ConfigurationViewModel

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
        model.configurations.forEach {
            groupAdapter.add(ConfigurationItem(it))
        }
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
}
