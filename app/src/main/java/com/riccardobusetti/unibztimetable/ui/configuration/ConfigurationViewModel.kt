package com.riccardobusetti.unibztimetable.ui.configuration

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.UserPrefs
import com.riccardobusetti.unibztimetable.domain.usecases.GetUserPrefsUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.PutUserPrefsUseCase
import com.riccardobusetti.unibztimetable.utils.URLUtils
import com.riccardobusetti.unibztimetable.utils.custom.AdvancedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

/**
 * Alias of the callback that will be called whenever the user clicks on a configuration item.
 */
typealias OnConfigurationClickCallback = (ConfigurationViewModel.Configuration) -> Boolean

class ConfigurationViewModel(
    private val context: Context,
    private val getUserPrefsUseCase: GetUserPrefsUseCase,
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
                handleStudyPlanConfiguration("unibz.it")
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

    val userPrefsMap = mutableMapOf<UserPrefs.Pref, String>()

    val loading = MutableLiveData<Boolean>()

    fun getUserPrefs() {
        viewModelScope.launchWithSupervisor {
            val work = async(Dispatchers.IO) {
                getUserPrefsUseCase.getUserPrefs()
            }

            val userPrefs = try {
                work.await()
            } catch (e: Exception) {
                null
            }

            userPrefs?.let {

            }
        }
    }

    fun putUserPrefs() {
        viewModelScope.launchWithSupervisor {
            val isSuccessful = withContext(Dispatchers.IO) {
                putUserPrefsUseCase.putUserPrefs(UserPrefs(userPrefsMap))
            }

            if (isSuccessful) {

            } else {

            }
        }
    }

    private fun handleUsernameConfiguration(username: String): Boolean {
        userPrefsMap[UserPrefs.Pref.USERNAME] = username

        return true
    }

    private fun handleStudyPlanConfiguration(url: String): Boolean {
        val urlValues = URLUtils.parseURL(url)

        userPrefsMap[UserPrefs.Pref.DEPARTMENT_ID] =
            urlValues[UserPrefs.Pref.DEPARTMENT_ID] ?: return false
        userPrefsMap[UserPrefs.Pref.DEGREE_ID] = urlValues[UserPrefs.Pref.DEGREE_ID] ?: return false
        userPrefsMap[UserPrefs.Pref.STUDY_PLAN_ID] =
            urlValues[UserPrefs.Pref.STUDY_PLAN_ID] ?: return false

        return true
    }
}