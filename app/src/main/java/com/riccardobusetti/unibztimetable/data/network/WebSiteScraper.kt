package com.riccardobusetti.unibztimetable.data.network

import android.util.Log
import com.riccardobusetti.unibztimetable.domain.entities.Course
import com.riccardobusetti.unibztimetable.domain.entities.Day
import com.riccardobusetti.unibztimetable.utils.DateUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.util.*

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
class WebSiteScraper(private val webSiteLink: WebSiteLink) {

    companion object {
        private const val TAG = "WebSiteScraper"
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

    private fun Element.selectCourseTitle() =
        this.select(CSSQueries.GET_COURSE_TITLE.cssQuery).text()

    private fun Element.selectCourseLocation() =
        this.select(CSSQueries.GET_COURSE_LOCATION.cssQuery).text()

    private fun Element.selectCourseProfessor() =
        this.select(CSSQueries.GET_COURSE_PROFESSOR.cssQuery).text()

    private fun Element.selectCourseTime() =
        this.select(CSSQueries.GET_COURSE_TIME_AND_TYPE.cssQuery).text().time()

    private fun Element.selectCourseType() =
        this.select(CSSQueries.GET_COURSE_TIME_AND_TYPE.cssQuery).text().type()

    /**
     * Gets the website as [Document] object which will contain all the [Element]s that
     * the algorithm is going to scrape.
     */
    private fun getWebSite(): Document {
        Log.d(TAG, "Scraping the website at url -> ${webSiteLink.url}")

        return Jsoup.connect(webSiteLink.url).get()
    }

    /**
     * Gets the timetable from the website [Document] and returns a [List] of [Day]
     * which represent the timetable.
     */
    fun getTimetable() = getAllDays(getWebSite())

    /**
     * Scrapes and computes all the days from the website.
     */
    private fun getAllDays(webSite: Document) = webSite.selectAllDays().map { day ->
        Day(
            date = formatDayDate(day.selectDayDate()),
            courses = getAllCourses(day)
        )
    }

    /**
     * Scrapes and computes all the courses from the website.
     */
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
        var prevLocation = "Error while getting location"

        return day.selectAllCourses().map { course ->
            val mappedCourse = Course(
                title = course.selectCourseTitle(),
                location = if (course.selectCourseLocation().isBlank()) prevLocation else course.selectCourseLocation(),
                time = course.selectCourseTime(),
                professor = course.selectCourseProfessor(),
                type = course.selectCourseType()
            )

            prevLocation = course.selectCourseLocation()

            return@map mappedCourse
        }
    }

    /**
     * Formats the day date in order to have a format which is easily converted to enable
     * any kind of date related features in the app.
     */
    private fun formatDayDate(date: String) = date
        .split(",")
        .joinToString(separator = ",") {
            var newValue = if (!it.contains(" "))
                it.take(3)
            else it

            newValue = if (DateUtils.getDefaultLocaleGuarded() == Locale.ITALY)
                newValue.toLowerCase(DateUtils.getDefaultLocaleGuarded())
            else
                newValue

            return@joinToString newValue
        }
}