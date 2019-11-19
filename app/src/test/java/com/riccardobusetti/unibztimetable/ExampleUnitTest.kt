package com.riccardobusetti.unibztimetable

import org.junit.Test
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun check_if_date_is_converted() {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-mm-dd", Locale.ITALIAN)
        val date = LocalDate.parse("2019-9-12", dateFormatter)
        println("$date")
    }

    @Test
    fun check_local_date() {
        val dateFormatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm", Locale.ITALY)
        val date = dateFormatter.format("Thu, 31 Oct 2019 08:00")
        println(date)
    }

    @Test
    fun check_current_locale() {
        val REGEX = "(https://www.unibz.it/)..(/timetable)"
        val regex = Regex(REGEX)

        val p = Pattern.compile(REGEX)
        val m = p.matcher("https://www.unibz.it/timetable")


        println(m.find())
    }
}
