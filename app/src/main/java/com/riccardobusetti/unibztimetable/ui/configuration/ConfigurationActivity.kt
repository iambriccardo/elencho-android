package com.riccardobusetti.unibztimetable.ui.configuration

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.UserPrefs
import com.riccardobusetti.unibztimetable.domain.repositories.UserPrefsRepository
import com.riccardobusetti.unibztimetable.domain.strategies.SharedPreferencesUserPrefsStrategy
import com.riccardobusetti.unibztimetable.domain.usecases.GetUserPrefsUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.PutUserPrefsUseCase
import com.riccardobusetti.unibztimetable.ui.items.ConfigurationItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_configuration.*

class ConfigurationActivity : AppCompatActivity() {

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private lateinit var model: ConfigurationViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var saveButton: Button

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
                GetUserPrefsUseCase(userPrefsRepository),
                PutUserPrefsUseCase(userPrefsRepository)
            )
        )
            .get(
                ConfigurationViewModel::class.java
            )
    }

    private fun attachObservers() {
        model.loading.observe(this, Observer {

        })

        model.success.observe(this, Observer {

        })

        model.userPrefs.observe(this, Observer {
            Log.d("NEW PREFS", "$it")
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
}
