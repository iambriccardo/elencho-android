package com.riccardobusetti.unibztimetable.utils

import android.os.Build
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

    private val supportedLocales = listOf("en", "de", "it")

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

    infix fun Calendar.addDays(days: Int) = this.apply { this.add(Calendar.DAY_OF_WEEK, days) }

    infix fun Calendar.addYears(years: Int) = this.apply { this.add(Calendar.YEAR, years) }

    fun getCurrentCalendar() = Calendar.getInstance()

    fun getCurrentDate() = getCurrentCalendar().time

    fun getCurrentDateFormatted(dateFormat: String = "yyyy-MM-dd") =
        formatDateToString(getCurrentDate(), dateFormat)

    fun getCurrentTimeFormatted(): String {
        val calendar = getCurrentCalendar()

        return "${getCurrentDateFormatted()} ${String.format(
            "%02d",
            calendar.get(Calendar.HOUR_OF_DAY)
        )}:${String.format("%02d", calendar.get(Calendar.MINUTE))}"
    }

    fun getCurrentDatePlusDaysFormatted(days: Int) =
        formatDateToString((getCurrentCalendar() addDays days).time)

    fun getCurrentDatePlusYearsFormatted(years: Int) =
        formatDateToString((getCurrentCalendar() addYears years).time)

    fun formatDateToString(date: Date, dateFormat: String = "yyyy-MM-dd"): String {
        val dateFormatter = SimpleDateFormat(dateFormat, getDefaultLocaleGuarded())

        return dateFormatter.format(date)
    }

    fun isCourseOnGoing(courseStartDate: String, courseEndDate: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formattedCourseStartDate = LocalDateTime.parse(
                courseStartDate,
                DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm", getDefaultLocaleGuarded())
            )
            val formattedCourseEndDate = LocalDateTime.parse(
                courseEndDate,
                DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm", getDefaultLocaleGuarded())
            )
            val formattedCurrentDate = LocalDateTime.parse(
                getCurrentTimeFormatted(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", getDefaultLocaleGuarded())
            )

            formattedCurrentDate in formattedCourseStartDate..formattedCourseEndDate
        } else {
            false
        }
    }
}