package com.riccardobusetti.unibztimetable.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.timePicker
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.ui.configuration.ConfigurationActivity
import java.util.*

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.app_preferences)
        setupListeners()
    }

    private fun setupListeners() {
        findPreference<Preference>("preference_configuration")?.setOnPreferenceClickListener {
            startActivity(Intent(context, ConfigurationActivity::class.java))
            true
        }

        val switchPreferenceCompat =
            findPreference<SwitchPreferenceCompat>("preference_daily_notification")

        switchPreferenceCompat?.setOnPreferenceChangeListener { _, newValue ->
            // TODO: implement preference saving.
            if (newValue as Boolean) {
                MaterialDialog(context!!).show {
                    timePicker { _, time ->
                        Toast.makeText(
                            context,
                            "${String.format("%02d", time.get(Calendar.HOUR_OF_DAY))}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            true
        }

        findPreference<Preference>("preference_contact")?.setOnPreferenceClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:riccardob36@gmail.com")
                startActivity(this)
            }
            true
        }
    }
}