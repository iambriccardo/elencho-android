package com.riccardobusetti.unibztimetable.ui.choosefaculty

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.app.AppSection
import com.riccardobusetti.unibztimetable.domain.entities.choices.FacultyChoice
import com.riccardobusetti.unibztimetable.domain.repositories.ChooseFacultyRepository
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository
import com.riccardobusetti.unibztimetable.domain.repositories.UserPrefsRepository
import com.riccardobusetti.unibztimetable.domain.strategies.LocalTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.RemoteTimetableStrategy
import com.riccardobusetti.unibztimetable.domain.strategies.SharedPreferencesUserPrefsStrategy
import com.riccardobusetti.unibztimetable.domain.usecases.DeleteLocalTimetableUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.PutUserPrefsUseCase
import com.riccardobusetti.unibztimetable.ui.custom.BackableFragment
import com.riccardobusetti.unibztimetable.ui.custom.BaseFragment
import com.riccardobusetti.unibztimetable.ui.items.FacultyItem
import com.riccardobusetti.unibztimetable.ui.setup.SetupActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_choose_faculty.*

class ChooseFacultyFragment : BaseFragment<ChooseFacultyViewModel>(), BackableFragment {

    override val appSection: AppSection
        get() = AppSection.CHOOSE_FACULTY

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var retryButton: Button
    private lateinit var continueButton: Button

    override fun initViewModel(): ChooseFacultyViewModel {
        val userPrefsRepository =
            UserPrefsRepository(SharedPreferencesUserPrefsStrategy(requireContext()))
        val timetableRepository = TimetableRepository(
            LocalTimetableStrategy(requireContext()),
            RemoteTimetableStrategy()
        )

        return ViewModelProviders.of(
            this,
            ChooseFacultyViewModelFactory(
                requireContext(),
                ChooseFacultyRepository(),
                PutUserPrefsUseCase(userPrefsRepository),
                DeleteLocalTimetableUseCase(timetableRepository)
            )
        ).get(ChooseFacultyViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_choose_faculty, container, false)
    }

    override fun setupUI() {
        progressBar = fragment_choose_faculty_progress_bar

        recyclerView = fragment_choose_faculty_recyclerview
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = groupAdapter
            layoutAnimation = AnimationUtils.loadLayoutAnimation(
                    activity,
                    R.anim.layout_animation_slide_from_bottom
            )
        }

        retryButton = fragment_choose_faculty_retry_button
        retryButton.setOnClickListener {
            model?.start()
        }

        continueButton = fragment_choose_faculty_continue_button
        continueButton.setOnClickListener {
            model?.saveUserPrefs()
            (requireActivity() as SetupActivity).nextSetupPhase()
        }
    }

    override fun attachObservers() {
        model?.let {
            it.choices.observe(this, Observer { choices ->
                showChoices(choices)
            })

            it.showContinueButton.observe(this, Observer { show ->
                if (show) {
                    showContinueButton()
                } else {
                    hideContinueButton()
                }
            })

            it.loading.observe(this, Observer { isLoading ->
                if (isLoading) {
                    showLoading()
                } else {
                    hideLoading()
                }
            })

            it.error.observe(this, Observer {
                showRetryButton()
                Snackbar.make(requireView(), R.string.error_message, Snackbar.LENGTH_LONG).show()
            })
        }
    }

    override fun onBackPressed() {
        model?.let {
            if (it.canGoBack()) {
                it.goBack()
            } else {
                (requireActivity() as SetupActivity).previousSetupPhase()
            }
        }
    }

    private fun showChoices(choices: List<FacultyChoice>) {
        groupAdapter.clear()
        choices.forEach { choice ->
            groupAdapter.add(FacultyItem(choice) { clickedChoice -> handleChoiceClick(clickedChoice) })
        }
        recyclerView.scheduleLayoutAnimation()
    }

    private fun handleChoiceClick(choice: FacultyChoice) {
        model?.select(choice)
    }

    private fun showContinueButton() {
        continueButton.visibility = View.VISIBLE
    }

    private fun hideContinueButton() {
        continueButton.visibility = View.GONE
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        hideRetryButton()
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private fun showRetryButton() {
        retryButton.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.GONE
    }

    private fun hideRetryButton() {
        retryButton.visibility = View.GONE
    }
}