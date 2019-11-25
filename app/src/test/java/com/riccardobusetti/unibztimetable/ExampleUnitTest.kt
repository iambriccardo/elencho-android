package com.riccardobusetti.unibztimetable

import org.junit.Test
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun check_if_date_is_converted() {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val date = LocalDateTime.parse("2019-10-30 14:00:00", dateFormatter)
        println("Before: $date")

        val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss")
        val formatDateTime = date.format(formatter)
        println("After : $formatDateTime")
    }

    @Test
    fun convert() {
        val oldDate = "Thu, 20 Feb 2019 10:00:00"
        val dateFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH)
        val date = LocalDateTime.parse(oldDate, dateFormatter)
        println("New date: $date")
    }

    @Test
    fun convert_less_26() {
        val formatter = SimpleDateFormat("EEE, dd MMM yyyy")
        val answer: String =
            formatter.format(SimpleDateFormat("EEE, dd MMM yyyy").parse("Thu, 20 Feb 2019"))
    }

    @Test
    fun check_local_date() {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val date = LocalDateTime.parse("2019-10-30 14:00:00", dateFormatter)
        println("${date.second}")
    }
}
