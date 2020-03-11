package com.riccardobusetti.unibztimetable.ui.configuration

import android.app.AlarmManager
import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.data.remote.WebSiteUrl
import com.riccardobusetti.unibztimetable.domain.entities.UserPrefs
import com.riccardobusetti.unibztimetable.domain.entities.params.UserPrefsParams
import com.riccardobusetti.unibztimetable.domain.usecases.DeleteLocalTimetableUseCase
import com.riccardobusetti.unibztimetable.domain.usecases.PutUserPrefsUseCase
import com.riccardobusetti.unibztimetable.receivers.AlarmReceiver
import com.riccardobusetti.unibztimetable.ui.custom.AdvancedViewModel
import com.riccardobusetti.unibztimetable.utils.AlarmUtils
import com.riccardobusetti.unibztimetable.utils.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Alias of the callback that will be called whenever the user clicks on a configuration item.
 */
typealias OnConfigurationClickCallback = (ConfigurationViewModel.Configuration, (Boolean) -> Unit) -> Unit

class ConfigurationViewModel(
    private val context: Context,
    private val putUserPrefsUseCase: PutUserPrefsUseCase,
    private val deleteLocalTimetableUseCase: DeleteLocalTimetableUseCase
) : AdvancedViewModel() {

    companion object {
        private const val TAG = "ConfigurationViewModel"
    }

    enum class Configuration(
        @IdRes @StringRes val titleResId: Int,
        @IdRes @StringRes val descriptionResId: Int,
        @IdRes @DrawableRes val iconResId: Int
    ) {
        STUDY_PLAN(
            R.string.configuration_study_plan_title,
            R.string.configuration_study_plan_description,
            R.drawable.ic_school
        ),
        DAILY_NOTIFICATION(
            R.string.configuration_daily_notification_title,
            R.string.configuration_daily_notification_description,
            R.drawable.ic_notifications
        )
    }

    val loading = MutableLiveData<Boolean>()

    val success = MutableLiveData<Boolean>()

    val userPrefs = MutableLiveData<Map<UserPrefs.Pref, String>>().apply { this.value = mapOf() }

    override fun start() {}

    fun putUserPrefs() {
        viewModelScope.safeLaunch(TAG) {
            loading.value = true

            val isSuccessful = withContext(Dispatchers.IO) {
                userPrefs.value?.let {
                    putUserPrefsUseCase.execute(
                        UserPrefsParams(
                            UserPrefs(it)
                        )
                    )

                    return@withContext true
                }

                return@withContext false
            }

            // If the preferences were successfully saved we will delete all the data in the
            // local timetable.
            if (isSuccessful) {
                withContext(Dispatchers.IO) {
                    deleteLocalTimetableUseCase.execute(null)
                }
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
                putUserPref(UserPrefs.Pref.DEPARTMENT_KEY, urlValues[UserPrefs.Pref.DEPARTMENT_KEY])
            result = putUserPref(UserPrefs.Pref.DEGREE_KEY, urlValues[UserPrefs.Pref.DEGREE_KEY])
            result =
                putUserPref(UserPrefs.Pref.STUDY_PLAN_KEY, urlValues[UserPrefs.Pref.STUDY_PLAN_KEY])
        }

        return result
    }

    fun handleDailyNotificationConfiguration(calendar: Calendar): Boolean {
        putUserPref(
            UserPrefs.Pref.DAILY_NOTIFICATION_TIME,
            "${DateUtils.formatTime(calendar.get(Calendar.HOUR_OF_DAY))}:${DateUtils.formatTime(
                calendar.get(Calendar.MINUTE)
            )}"
        )

        // TODO: handle alarm set before user prefs writing.
        AlarmUtils.cancelAlarm(context, AlarmUtils::class.java)
        AlarmUtils.scheduleRepeatingAlarm(
            context,
            AlarmReceiver::class.java,
            calendar,
            AlarmManager.INTERVAL_DAY
        )

        return true
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