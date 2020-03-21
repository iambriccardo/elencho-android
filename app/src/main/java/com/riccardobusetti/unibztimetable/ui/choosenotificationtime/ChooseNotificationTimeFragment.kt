package com.riccardobusetti.unibztimetable.ui.choosenotificationtime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.timePicker
import com.google.android.material.snackbar.Snackbar
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.app.AppSection
import com.riccardobusetti.unibztimetable.domain.repositories.UserPrefsRepository
import com.riccardobusetti.unibztimetable.domain.strategies.SharedPreferencesUserPrefsStrategy
import com.riccardobusetti.unibztimetable.domain.usecases.PutUserPrefsUseCase
import com.riccardobusetti.unibztimetable.ui.custom.BackableFragment
import com.riccardobusetti.unibztimetable.ui.custom.BaseFragment
import com.riccardobusetti.unibztimetable.ui.setup.SetupActivity
import com.riccardobusetti.unibztimetable.utils.DateUtils
import kotlinx.android.synthetic.main.fragment_choose_notification_time.*

class ChooseNotificationTimeFragment : BaseFragment<ChooseNotificationTimeViewModel>(),
    BackableFragment {

    override val appSection: AppSection
        get() = AppSection.CHOOSE_NOTIFICATION_TIME

    private lateinit var chooseTimeButton: Button
    private lateinit var continueButton: Button

    override fun initViewModel(): ChooseNotificationTimeViewModel {
        val userPrefsRepository =
            UserPrefsRepository(SharedPreferencesUserPrefsStrategy(requireContext()))

        return ViewModelProviders.of(
            this,
            ChooseNotificationTimeViewModelFactory(
                requireContext(),
                PutUserPrefsUseCase(userPrefsRepository)
            )
        ).get(ChooseNotificationTimeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_choose_notification_time, container, false)
    }

    override fun setupUI() {
        chooseTimeButton = fragment_choose_notification_time_choose_time_button
        chooseTimeButton.setOnClickListener {
            openTimeDialog()
        }

        continueButton = fragment_choose_notification_time_continue_button
        continueButton.setOnClickListener {
            model?.saveUserPrefs()
            (requireActivity() as SetupActivity).nextSetupPhase()
        }
    }

    override fun attachObservers() {
        model?.let {
            it.showContinueButton.observe(this, Observer { show ->
                if (show) {
                    showContinueButton()
                } else {
                    hideContinueButton()
                }
            })

            it.selectedTime.observe(this, Observer { selectedTime ->
                chooseTimeButton.text = selectedTime
            })

            it.error.observe(this, Observer {
                Snackbar.make(requireView(), R.string.error_message, Snackbar.LENGTH_LONG).show()
            })
        }
    }

    override fun onBackPressed() {
        (requireActivity() as SetupActivity).previousSetupPhase()
    }

    private fun openTimeDialog() {
        MaterialDialog(requireContext()).show {
            timePicker(currentTime = DateUtils.getCurrentCalendar()) { _, datetime ->
                model?.selectNotificationTime(datetime)
            }
        }
    }

    private fun showContinueButton() {
        continueButton.visibility = View.VISIBLE
    }

    private fun hideContinueButton() {
        continueButton.visibility = View.GONE
    }
}