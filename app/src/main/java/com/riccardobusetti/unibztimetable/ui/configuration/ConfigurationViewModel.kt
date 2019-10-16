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
typealias OnConfigurationClickCallback = (ConfigurationViewModel.Configuration) -> Boolean

class ConfigurationViewModel(
    private val context: Context,
    private val putUserPrefsUseCase: PutUserPrefsUseCase
) : AdvancedViewModel() {

    data class Configuration(
        val id: Int,
        val title: String,
        val description: String,
        @IdRes @DrawableRes val iconResId: Int,
        val clickCallback: OnConfigurationClickCallback
    )

    private val handleConfigurationClick: OnConfigurationClickCallback = {
        when (it.id) {
            1 -> {
                // TODO: show dialog.
                handleUsernameConfiguration("Riccardo")
            }
            2 -> {
                // TODO: show dialog.
                handleStudyPlanConfiguration("https://www.unibz.it/en/timetable/?department=22&degree=13205&studyPlan=16858&fromDate=2019-10-08&toDate=2019-10-14&page=1")
            }
            else -> false
        }
    }

    val configurations = listOf(
        Configuration(
            1,
            "Write your name",
            "This is needed in order to further personalize the experience",
            R.drawable.ic_face,
            handleConfigurationClick
        ),
        Configuration(
            2,
            "Choose your study plan",
            "In order to see the timetable you need to select your study plan",
            R.drawable.ic_school,
            handleConfigurationClick
        )
    )

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

    private fun handleUsernameConfiguration(username: String) =
        putUserPref(UserPrefs.Pref.USERNAME, username)

    private fun handleStudyPlanConfiguration(url: String): Boolean {
        val urlValues = WebSiteUrl.parseUrl(url)

        var result: Boolean

        result = putUserPref(UserPrefs.Pref.DEPARTMENT_ID, urlValues[UserPrefs.Pref.DEPARTMENT_ID])
        result = putUserPref(UserPrefs.Pref.DEGREE_ID, urlValues[UserPrefs.Pref.DEGREE_ID])
        result = putUserPref(UserPrefs.Pref.STUDY_PLAN_ID, urlValues[UserPrefs.Pref.STUDY_PLAN_ID])

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