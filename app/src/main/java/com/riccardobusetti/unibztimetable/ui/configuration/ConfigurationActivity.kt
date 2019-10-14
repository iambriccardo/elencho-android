package com.riccardobusetti.unibztimetable.ui.configuration

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.ui.items.ConfigurationItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_configuration.*

class ConfigurationActivity : AppCompatActivity() {

    data class Configuration(
        val title: String,
        val description: String,
        @IdRes @DrawableRes val iconResId: Int,
        val clickCallback: () -> Boolean
    )

    private val configurations = listOf(
        Configuration(
            "Write your name",
            "This is needed in order to further personalize the experience",
            R.drawable.ic_face
        ) {
            true
        },
        Configuration(
            "Choose your study plan",
            "In order to see the timetable you need to select your study plan",
            R.drawable.ic_school
        ) {
            true
        }
    )

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private lateinit var model: ConfigurationViewModel

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration)

        setupModel()
        setupUi()
    }

    private fun setupModel() {
        model = ViewModelProviders.of(
            this,
            ConfigurationViewModelFactory()
        )
            .get(
                ConfigurationViewModel::class.java
            )
    }

    private fun setupUi() {
        recyclerView = activity_configuration_recycler_view
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ConfigurationActivity)
            adapter = groupAdapter
        }

        configurations.forEach {
            groupAdapter.add(ConfigurationItem(it))
        }
    }
}
