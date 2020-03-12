package com.riccardobusetti.unibztimetable.ui.choosenotificationtime

import android.app.AlarmManager
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.riccardobusetti.unibztimetable.domain.entities.UserPrefs
import com.riccardobusetti.unibztimetable.domain.entities.params.UserPrefsParams
import com.riccardobusetti.unibztimetable.domain.usecases.PutUserPrefsUseCase
import com.riccardobusetti.unibztimetable.receivers.AlarmReceiver
import com.riccardobusetti.unibztimetable.ui.custom.BaseViewModel
import com.riccardobusetti.unibztimetable.utils.AlarmUtils
import com.riccardobusetti.unibztimetable.utils.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class ChooseNotificationTimeViewModel(
    private val context: Context,
    private val putUserPrefsUseCase: PutUserPrefsUseCase
) : BaseViewModel() {

    companion object {
        private const val TAG = "ChooseNotificationTimeViewModel"
    }

    private val _showContinueButton = MutableLiveData<Boolean>(false)
    val showContinueButton: LiveData<Boolean>
        get() = _showContinueButton

    private val _selectedTime = MutableLiveData<String>()
    val selectedTime: LiveData<String>
        get() = _selectedTime

    private var selectedCalendar: Calendar? = null

    override fun start() {}

    fun selectNotificationTime(calendar: Calendar) {
        selectedCalendar = calendar
        _selectedTime.value = formatCalendar(calendar)
        showContinueButton()
    }

    fun saveUserPrefs() {
        viewModelScope.safeLaunch(TAG) {
            withContext(Dispatchers.IO) {
                putUserPrefsUseCase.execute(UserPrefsParams(buildUserPrefs()))
                scheduleAlarm()
            }
        }
    }

    private fun buildUserPrefs(): UserPrefs {
        val userPrefsMap = mutableMapOf<UserPrefs.Pref, String>()

        selectedCalendar?.let {
            userPrefsMap[UserPrefs.Pref.DAILY_NOTIFICATION_TIME] = formatCalendar(it)
        }

        return UserPrefs(userPrefsMap)
    }

    private fun scheduleAlarm() {
        selectedCalendar?.let {
            AlarmUtils.cancelAlarm(context, AlarmUtils::class.java)
            AlarmUtils.scheduleRepeatingAlarm(
                context,
                AlarmReceiver::class.java,
                it,
                AlarmManager.INTERVAL_DAY
            )
        }
    }

    private fun formatCalendar(calendar: Calendar) =
        "${DateUtils.formatTime(calendar.get(Calendar.HOUR_OF_DAY))}:${DateUtils.formatTime(
            calendar.get(
                Calendar.MINUTE
            )
        )}"

    private fun showContinueButton() {
        _showContinueButton.value = true
    }

    private fun hideContinueButton() {
        _showContinueButton.value = false
    }
}
