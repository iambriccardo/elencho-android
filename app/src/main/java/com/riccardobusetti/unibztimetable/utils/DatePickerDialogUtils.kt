package com.riccardobusetti.unibztimetable.utils

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import com.riccardobusetti.unibztimetable.ui.timemachine.TimeMachineViewModel
import java.util.*

/**
 * Alias of the callback that will be called whenever a new date has been setted.
 */
typealias OnDateSetCallback = (String, TimeMachineViewModel.DatePickerState) -> Unit

/**
 * Alias of the callback that will be called whenever the dialog is closed.
 */
typealias OnDialogClosedCallback = () -> Unit

class DatePickerDialogUtils(
    context: Context,
    private val dateSelectedCallback: OnDateSetCallback,
    private val dialogCloseCallback: OnDialogClosedCallback
) {

    companion object {
        private const val TAG = "DatePickerDialogUtils"
    }

    private fun Int.addLeadingZero() = if (this.toString().length < 2) "0${this}" else "${this}"

    private var datePickerState = TimeMachineViewModel.DatePickerState.CLOSED
    private val dialog: DatePickerDialog

    init {
        val calendar = DateUtils.getCurrentCalendar()

        dialog = DatePickerDialog(
            context,
            getListener(),
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        dialog.setOnDismissListener {
            this.datePickerState = TimeMachineViewModel.DatePickerState.CLOSED
            dialogCloseCallback()
        }
    }

    fun show(datePickerState: TimeMachineViewModel.DatePickerState) {
        this.datePickerState = datePickerState
        dialog.show()
    }

    fun hide() {
        dialog.hide()
    }

    private fun getListener() = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        val monthOfTheYear = month + 1

        Log.d(
            TAG,
            "The user selected a date with the following parameters: year -> $year, month -> $monthOfTheYear, day-> $dayOfMonth"
        )

        dateSelectedCallback(
            "$year-${monthOfTheYear.addLeadingZero()}-${dayOfMonth.addLeadingZero()}",
            datePickerState
        )
    }
}
