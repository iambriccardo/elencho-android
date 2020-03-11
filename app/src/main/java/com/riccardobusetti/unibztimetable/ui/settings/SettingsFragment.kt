package com.riccardobusetti.unibztimetable.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.ui.configuration.ConfigurationActivity
import com.riccardobusetti.unibztimetable.ui.setup.SetupActivity

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.app_preferences)
        setupListeners()
    }

    private fun setupListeners() {
        findPreference<Preference>("preference_configuration")?.setOnPreferenceClickListener {
            Intent(context, SetupActivity::class.java).apply {
                putExtra(ConfigurationActivity.IS_FIRST_CONFIGURATION_KEY, false)
                startActivity(this)
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