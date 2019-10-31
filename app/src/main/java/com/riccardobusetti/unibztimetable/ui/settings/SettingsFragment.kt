package com.riccardobusetti.unibztimetable.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.ui.configuration.ConfigurationActivity

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.app_preferences)
        setupUi()
    }

    private fun setupUi() {
        findPreference<Preference>("preference_configuration")?.setOnPreferenceClickListener {
            startActivity(Intent(context, ConfigurationActivity::class.java))
            true
        }
    }
}