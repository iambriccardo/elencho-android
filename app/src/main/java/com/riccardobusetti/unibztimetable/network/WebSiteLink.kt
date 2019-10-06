package com.riccardobusetti.unibztimetable.network

import com.riccardobusetti.unibztimetable.ui.utils.DateUtils
import java.util.*

/**
 * Class responsible of building a url to get the timetable from the unibz
 * website.
 *
 * @author Riccardo Busetti
 */
class WebSiteLink private constructor(val url: String) {

    companion object {

        private const val BASE_URL = "https://www.unibz.it"

        private const val TIMETABLE_URL_PATH = "timetable"

        private const val DEPARTMENT_URL_PARAM = "department"
        private const val DEGREE_URL_PARAM = "degree"
        private const val ACADEMIC_YEAR_URL_PARAM = "studyPlan"
        private const val FROM_DATE_URL_PARAM = "fromDate"
        private const val TO_DATE_URL_PARAM = "toDate"
        private const val PAGE_URL_PARAM = "page"
    }

    /**
     * Nested class which is used to build the [WebSiteLink] for the remote timetable request.
     */
    data class Builder(
        var language: String = "en",
        var department: String = "",
        var degree: String = "",
        var academicYear: String = "",
        var fromDate: String = "",
        var toDate: String = "",
        var page: String = "1"
    ) {
        private fun String.encodeComma(): String {
            return if (this.contains(","))
                this.replace(",", "%2C")
            else
                this
        }

        private fun getTodayDate() = DateUtils.getCurrentDateFormatted()

        fun useDeviceLanguage() = apply { this.language = Locale.getDefault().language }

        fun withLanguage(language: String) = apply { this.language = language }

        fun withDepartment(department: String) = apply { this.department = department }

        fun withDegree(degree: String) = apply { this.degree = degree }

        fun withAcademicYear(academicYear: String) = apply { this.academicYear = academicYear }

        fun onlyToday() = apply {
            this.fromDate = getTodayDate()
            this.toDate = getTodayDate()
        }

        fun fromToday() = apply { this.fromDate = getTodayDate() }

        fun fromDate(fromDate: String) = apply { this.fromDate = fromDate }

        fun toNext7Days() = apply { this.toDate = DateUtils.getCurrentDatePlusDaysFormatted(7) }

        fun toOneYear() = apply { this.toDate = DateUtils.getCurrentDatePlusYearsFormatted(1) }

        fun toDate(toDate: String) = apply { this.toDate = toDate }

        fun atPage(page: String) = apply { this.page = page }

        fun build() = WebSiteLink(
            BASE_URL +
                    "/$language" +
                    "/$TIMETABLE_URL_PATH" +
                    "/?$DEPARTMENT_URL_PARAM=${this.department.encodeComma()}" +
                    "&$DEGREE_URL_PARAM=${this.degree.encodeComma()}" +
                    "&$ACADEMIC_YEAR_URL_PARAM=${this.academicYear.encodeComma()}" +
                    "&$FROM_DATE_URL_PARAM=${this.fromDate}" +
                    "&$TO_DATE_URL_PARAM=${this.toDate}" +
                    "&$PAGE_URL_PARAM=${this.page}"
        )
    }

    override fun toString() = this.url
}