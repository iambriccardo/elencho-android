package com.riccardobusetti.unibztimetable.ui.configuration

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.data.network.WebSiteUrl
import com.riccardobusetti.unibztimetable.domain.entities.UserPrefs
import com.riccardobusetti.unibztimetable.domain.usecases.PutUserPrefsUseCase
import com.riccardobusetti.unibztimetable.utils.custom.AdvancedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Alias of the callback that will be called whenever the user clicks on a configuration item.
 */
typealias OnConfigurationClickCallback = (ConfigurationViewModel.Configuration, (Boolean) -> Unit) -> Unit

class ConfigurationViewModel(
    private val context: Context,
    private val putUserPrefsUseCase: PutUserPrefsUseCase
) : AdvancedViewModel() {

    enum class Configuration(
        val title: String,
        val description: String,
        @IdRes @DrawableRes val iconResId: Int
    ) {
        STUDY_PLAN(
            "Choose your study plan",
            "In order to see the timetable you need to select your study plan",
            R.drawable.ic_school
        )
    }

    val loading = MutableLiveData<Boolean>()

    val success = MutableLiveData<Boolean>()

    val userPrefs = MutableLiveData<Map<UserPrefs.Pref, String>>().apply { this.value = mapOf() }

    fun putUserPrefs() {
        viewModelScope.launchWithSupervisor {
            loading.value = true

            val isSuccessful = withContext(Dispatchers.IO) {
                userPrefs.value?.let {
                    putUserPrefsUseCase.putUserPrefs(UserPrefs(it))

                    return@withContext true
                }

                return@withContext false
            }

            loading.value = false
            success.value = isSuccessful
        }
    }

    fun handleStudyPlanConfiguration(url: String): Boolean {
        val urlValues = WebSiteUrl.parseUrl(url)

        var result = false

        urlValues?.let {
            result =
                putUserPref(UserPrefs.Pref.DEPARTMENT_ID, urlValues[UserPrefs.Pref.DEPARTMENT_ID])
            result = putUserPref(UserPrefs.Pref.DEGREE_ID, urlValues[UserPrefs.Pref.DEGREE_ID])
            result =
                putUserPref(UserPrefs.Pref.STUDY_PLAN_ID, urlValues[UserPrefs.Pref.STUDY_PLAN_ID])
        }

        return result
    }

    /**
     * Puts a new user pref inside of the mutable live data object that holds a map with all the
     * current configured prefs.
     */
    private fun putUserPref(pref: UserPrefs.Pref, value: String?): Boolean {
        if (value == null) return false

        userPrefs.value?.let { oldPrefs ->
            val newPrefs = mutableMapOf(pref to value)
            oldPrefs.forEach { newPrefs[it.key] = it.value }

            userPrefs.value = newPrefs

            return true
        }

        return false
    }
}