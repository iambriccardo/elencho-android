package com.riccardobusetti.unibztimetable.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Class containing helper methods to manage dates.
 *
 * @author Riccardo Busetti
 */
object DateUtils {

    private infix fun Calendar.addDays(days: Int) =
        this.apply { this.add(Calendar.DAY_OF_WEEK, days) }

    private infix fun Calendar.addYears(years: Int) = this.apply { this.add(Calendar.YEAR, years) }

    private const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd"

    // TODO: support german (de) after the fix of date parsing.
    private val supportedLocales = listOf("en", "it")

    /**
     * Gets the [Locale.ENGLISH] only if the device has the other languages not supported. This is
     * done because the website supports only english, german and italian timetable.
     */
    fun getDefaultLocaleGuarded(): Locale {
        return if (supportedLocales.contains(Locale.getDefault().language)) {
            Locale.getDefault()
        } else {
            Locale.ENGLISH
        }
    }

    fun getCurrentCalendar() = Calendar.getInstance()

    fun getCurrentDate() = getCurrentCalendar().time

    fun getCurrentYear() = getCurrentCalendar().get(Calendar.YEAR)

    fun getCurrentDateFormatted(dateFormat: String = DEFAULT_DATE_FORMAT) =
        formatDateToString(getCurrentDate(), dateFormat)

    fun getCurrentTimeFormatted(useZeroTime: Boolean = false): String {
        val calendar = getCurrentCalendar()

        val minutes = if (useZeroTime) {
            "00:00"
        } else {
            "${String.format(
                "%02d",
                calendar.get(Calendar.HOUR_OF_DAY)
            )}:${String.format("%02d", calendar.get(Calendar.MINUTE))}"
        }

        return "${getCurrentDateFormatted()} $minutes"
    }

    fun getCurrentDatePlusDaysFormatted(days: Int) =
        formatDateToString((getCurrentCalendar() addDays days).time)

    fun getCurrentDatePlusYearsFormatted(years: Int) =
        formatDateToString((getCurrentCalendar() addYears years).time)

    fun getCalendarFromDate(date: Date): Calendar {
        val calendar = Calendar.getInstance()
        calendar.time = date

        return calendar
    }

    fun formatDateToString(date: Date, dateFormat: String = DEFAULT_DATE_FORMAT): String {
        val dateFormatter = SimpleDateFormat(dateFormat, getDefaultLocaleGuarded())

        return dateFormatter.format(date)
    }

    fun formatStringToDate(date: String, dateFormat: String = DEFAULT_DATE_FORMAT): Date? {
        val dateFormatter = SimpleDateFormat(dateFormat, getDefaultLocaleGuarded())

        return dateFormatter.parse(date)
    }

    fun mergeDayAndCourseTimeData(dayDate: String, courseTime: String) =
        "$dayDate ${getCurrentCalendar().get(Calendar.YEAR)} $courseTime"

    fun isDayPassed(dayDate: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formattedDayDate = parseLocalDateTime(dayDate, "EEE, dd MMM yyyy HH:mm")
            val formattedCurrentDate = parseLocalDateTime(
                getCurrentTimeFormatted(true),
                "yyyy-MM-dd HH:mm"
            )

            formattedDayDate < formattedCurrentDate
        } else {
            false
        }
    }

    fun isCourseFinished(courseEndDate: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formattedCourseEndDate = parseLocalDateTime(courseEndDate, "EEE, dd MMM yyyy HH:mm")
            val formattedCurrentDate =
                parseLocalDateTime(getCurrentTimeFormatted(), "yyyy-MM-dd HH:mm")

            formattedCurrentDate > formattedCourseEndDate
        } else {
            false
        }
    }

    fun isCourseOnGoing(courseStartDate: String, courseEndDate: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formattedCourseStartDate =
                parseLocalDateTime(courseStartDate, "EEE, dd MMM yyyy HH:mm")
            val formattedCourseEndDate = parseLocalDateTime(courseEndDate, "EEE, dd MMM yyyy HH:mm")
            val formattedCurrentDate =
                parseLocalDateTime(getCurrentTimeFormatted(), "yyyy-MM-dd HH:mm")

            formattedCurrentDate in formattedCourseStartDate..formattedCourseEndDate
        } else {
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseLocalDateTime(date: String, pattern: String) =
        LocalDateTime.parse(date, getDateTimeFormatter(pattern))

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDateTimeFormatter(pattern: String) =
        DateTimeFormatter.ofPattern(pattern, getDefaultLocaleGuarded())
}