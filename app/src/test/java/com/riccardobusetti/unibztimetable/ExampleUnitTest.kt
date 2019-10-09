package com.riccardobusetti.unibztimetable

import com.riccardobusetti.unibztimetable.utils.DateUtils
import org.junit.Test
import java.text.SimpleDateFormat
import java.time.LocalDate
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
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-mm-dd", Locale.ITALIAN)
        val date = LocalDate.parse("2019-9-12", dateFormatter)
        println("$date")
    }

    @Test
    fun check_local_date() {
        val dateFormatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm", Locale.ITALY)
        val date = dateFormatter.format(Calendar.getInstance().time)
        println(date)
    }

    @Test
    fun check_current_locale() {
        println(Locale.getDefault().language)
    }

    @Test
    fun check_if_lesson_is_going() {
        println(DateUtils.isCourseOnGoing("lun, 07 ott 2019 17:00", "lun, 07 ott 2019 18:02"))
    }
}
