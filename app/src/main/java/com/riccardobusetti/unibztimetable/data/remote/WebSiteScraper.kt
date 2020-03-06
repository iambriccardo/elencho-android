package com.riccardobusetti.unibztimetable.data.remote

import android.annotation.TargetApi
import android.os.Build
import android.util.Log
import com.riccardobusetti.unibztimetable.domain.entities.Course
import com.riccardobusetti.unibztimetable.utils.DateUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.threeten.bp.LocalDateTime

/**
 * Class containing all the logic for the scraping of the unibz website.
 *
 * This scraping is needed because as of now we don't have any APIs from the website.
 *
 * Important: when APIs are available, please remove this class because this scraping algorithm
 * is highly unreliable and can be broken by simply changing the html atPage.
 *
 * @author Riccardo Busetti
 */
class WebSiteScraper(private val webSiteUrl: WebSiteUrl) {

    companion object {
        private const val TAG = "WebSiteScraper"

        private const val NO_LOCATION = "N/A"
    }

    /**
     * Enum containing all the css queries we are going to perform
     * on the html document.
     *
     * @author Riccardo Busetti
     */
    enum class CSSQueries(val cssQuery: String) {
        GET_ALL_DAYS("article"),
        GET_ALL_COURSES(".u-pbi-avoid"),
        GET_DAY_DATE("h2"),
        GET_COURSE_TITLE(".u-push-btm-1"),
        GET_COURSE_LOCATION(".u-push-btm-quarter"),
        GET_COURSE_PROFESSOR(".actionLink"),
        GET_COURSE_TIME_AND_TYPE(".u-push-btm-none:first-of-type")
    }

    private fun String.compact() = this.replace(" ", "")

    private fun String.divideByDot() = this.split("·")

    private fun String.time() = this.compact().divideByDot()[0]

    private fun String.type() = this.compact().divideByDot()[1]

    private fun Element.selectAllDays() = this.select(CSSQueries.GET_ALL_DAYS.cssQuery)

    private fun Element.selectAllCourses() = this.select(CSSQueries.GET_ALL_COURSES.cssQuery)

    private fun Element.selectDayDate() = this.select(CSSQueries.GET_DAY_DATE.cssQuery).text()

    private fun Element.selectDayDateFormatted() = formatDate(this.selectDayDate())

    private fun Element.selectCourseDescription() =
        this.select(CSSQueries.GET_COURSE_TITLE.cssQuery).text()

    private fun Element.selectCourseRoom() =
        this.select(CSSQueries.GET_COURSE_LOCATION.cssQuery).text()

    private fun Element.selectCourseProfessor() =
        this.select(CSSQueries.GET_COURSE_PROFESSOR.cssQuery).text()

    private fun Element.selectCourseTime() =
        this.select(CSSQueries.GET_COURSE_TIME_AND_TYPE.cssQuery).text().time()

    private fun Element.selectCourseStartTime() = this.selectCourseTime().split("-")[0]

    private fun Element.selectCourseEndTime() = this.selectCourseTime().split("-")[1]

    private fun Element.selectCourseType() =
        this.select(CSSQueries.GET_COURSE_TIME_AND_TYPE.cssQuery).text().type()

    /**
     * Gets the website as [Document] object which will contain all the [Element]s that
     * the algorithm is going to scrape.
     */
    private fun getWebSite(): Document {
        Log.d(TAG, "Scraping the website at url -> ${webSiteUrl.url}")

        return Jsoup.connect(webSiteUrl.url).get()
    }

    /**
     * Gets the timetable from the website [Document] and returns a [List] of [Day]
     * which represent the timetable.
     */
    fun getTimetable() = getAll(getWebSite())

    /**
     * Scrapes and computes all the days from the website.
     */
    private fun getAll(webSite: Document) = webSite.selectAllDays().flatMap { day ->
        getAllCourses(day)
    }

    /**
     * Scrapes and computes all the courses from the website.
     */
    @TargetApi(Build.VERSION_CODES.O)
    private fun getAllCourses(day: Element): List<Course> {
        // This variable is used because there is a possibility in which
        // the location is not specified, thus we will use the previous location.
        // This is done because the previous location following the website design
        // corresponds to the same one.
        // For example:
        // BZ C2.01
        //  Mathematics I
        //  Mathematics II
        // These two courses are on the same class but on the HTML the location is only contained once.
        var prevRoom = NO_LOCATION
        val currentYear = DateUtils.getCurrentYear()

        return day.selectAllCourses().map { course ->
            val mappedCourse = Course(
                startDateTime = convertDate(
                    day.selectDayDateFormatted(),
                    currentYear,
                    course.selectCourseStartTime()
                ),
                endDateTime = convertDate(
                    day.selectDayDateFormatted(),
                    currentYear,
                    course.selectCourseEndTime()
                ),
                room = if (course.selectCourseRoom().isBlank()) prevRoom else course.selectCourseRoom(),
                description = course.selectCourseDescription(),
                professor = course.selectCourseProfessor(),
                type = course.selectCourseType()
            )

            prevRoom =
                if (course.selectCourseRoom().isBlank()) NO_LOCATION else course.selectCourseRoom()

            return@map mappedCourse
        }
    }

    /**
     * Formats the day date in order to have a format which is easily converted to enable
     * any kind of date related features in the app.
     */
    private fun formatDate(date: String) = date
        .split(",")
        .joinToString(separator = ",") {
            return@joinToString if (!it.contains(" "))
                it.take(3)
            else it
        }

    /**
     * Converts the course date and time into a [LocalDateTime] object that will contain all
     * the necessary information.
     *
     * If an exception is thrown it means that we are converting a date from the next year, thus
     * we call recursively the function with another year. We will stop the recursion when we tried
     * the next year only.
     *
     * So if the 2019 will fail we will use 2020 and if it doesn't work we return [LocalDateTime.MIN].
     */
    private fun convertDate(date: String, year: Int, time: String): LocalDateTime {
        try {
            if (year > DateUtils.getCurrentYear() + 1) return LocalDateTime.MIN

            return DateUtils.parseCourseDateTime(date, year, time)
        } catch (e: Exception) {
            return DateUtils.parseCourseDateTime(date, (year + 1), time)
        }
    }
}