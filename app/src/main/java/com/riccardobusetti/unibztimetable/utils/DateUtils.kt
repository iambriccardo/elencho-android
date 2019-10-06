package com.riccardobusetti.unibztimetable.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Class containing helper methods to manage dates.
 *
 * @author Riccardo Busetti
 */
object DateUtils {

    infix fun Calendar.addDays(days: Int) = this.apply { this.add(Calendar.DAY_OF_WEEK, days) }

    infix fun Calendar.addYears(years: Int) = this.apply { this.add(Calendar.YEAR, years) }

    fun getCurrentDate() = Calendar.getInstance().time

    fun getCurrentDateFormatted() = formatDate(Calendar.getInstance().time)

    fun getCurrentDatePlusDaysFormatted(days: Int) =
        formatDate((Calendar.getInstance() addDays days).time)

    fun getCurrentDatePlusYearsFormatted(years: Int) =
        formatDate((Calendar.getInstance() addYears years).time)

    fun formatDate(date: Date): String {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        return dateFormatter.format(date)
    }
}