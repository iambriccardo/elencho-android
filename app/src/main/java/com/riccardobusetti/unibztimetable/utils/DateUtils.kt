package com.riccardobusetti.unibztimetable.utils

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Class containing helper methods to manage dates.
 *
 * @author Riccardo Busetti
 */
object DateUtils {

    const val WEBSITE_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm"
    const val URL_DATE_FORMAT = "yyyy-MM-dd"
    private val LOCAL_CALENDAR_FORMAT = "yyyy-MM-dd HH:mm"

    private val supportedLocales = listOf("en")

    private infix fun Calendar.addDays(days: Int) =
        this.apply { this.add(Calendar.DAY_OF_WEEK, days) }

    private infix fun Calendar.addYears(years: Int) = this.apply { this.add(Calendar.YEAR, years) }

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

    fun getCurrentDateFormatted(dateFormat: String = URL_DATE_FORMAT) =
        formatDateToString(getCurrentDate(), dateFormat)

    fun getCurrentTimeFormatted(isMidnight: Boolean = false): String {
        val calendar = getCurrentCalendar()

        val minutes = if (isMidnight) {
            "00:00"
        } else {
            "${String.format(
                "%02d",
                calendar.get(Calendar.HOUR_OF_DAY)
            )}:${String.format("%02d", calendar.get(Calendar.MINUTE))}"
        }

        return "${getCurrentDateFormatted()} $minutes"
    }

    fun getCurrentLocalDateTime(isMidnight: Boolean = false): LocalDateTime =
        parseLocalDateTime(getCurrentTimeFormatted(isMidnight), LOCAL_CALENDAR_FORMAT)

    fun getCurrentDatePlusDaysFormatted(days: Int) =
        formatDateToString((getCurrentCalendar() addDays days).time)

    fun getCurrentDatePlusYearsFormatted(years: Int) =
        formatDateToString((getCurrentCalendar() addYears years).time)

    fun getCalendarFromDate(date: Date): Calendar {
        val calendar = Calendar.getInstance()
        calendar.time = date

        return calendar
    }

    fun formatDateToString(date: Date, dateFormat: String = URL_DATE_FORMAT): String {
        val dateFormatter = SimpleDateFormat(dateFormat, getDefaultLocaleGuarded())

        return dateFormatter.format(date)
    }

    fun formatStringToDate(date: String, dateFormat: String = URL_DATE_FORMAT): Date? {
        val dateFormatter = SimpleDateFormat(dateFormat, getDefaultLocaleGuarded())

        return dateFormatter.parse(date)
    }

    private fun parseLocalDateTime(date: String, pattern: String): LocalDateTime =
        LocalDateTime.parse(date, getDateTimeFormatter(pattern))

    private fun getDateTimeFormatter(pattern: String): DateTimeFormatter =
        DateTimeFormatter.ofPattern(pattern, getDefaultLocaleGuarded())

    fun convertCourseDate(date: String, year: Int = getCurrentYear(), time: String): LocalDateTime {
        val dateTime = "$date $year $time"
        val dateFormatter = DateTimeFormatter.ofPattern(WEBSITE_DATE_FORMAT, Locale.ENGLISH)

        return LocalDateTime.parse(dateTime, dateFormatter)
    }
}