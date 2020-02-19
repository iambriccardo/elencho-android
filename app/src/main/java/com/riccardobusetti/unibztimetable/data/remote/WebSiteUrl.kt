package com.riccardobusetti.unibztimetable.data.remote

import android.net.Uri
import com.riccardobusetti.unibztimetable.domain.entities.UserPrefs
import com.riccardobusetti.unibztimetable.utils.DateUtils
import com.riccardobusetti.unibztimetable.utils.StringUtils
import java.util.*

/**
 * Class responsible of building a url to get the timetable from the unibz
 * website.
 *
 * @author Riccardo Busetti
 */
class WebSiteUrl private constructor(val url: String) {

    companion object {

        private const val BASE_URL = "https://www.unibz.it"

        private const val TIMETABLE_URL_PATH = "timetable"

        private const val SEARCH_BY_KEYWORDS_URL_PARAM = "searchByKeywords"
        private const val DEPARTMENT_URL_PARAM = "department"
        private const val DEGREE_URL_PARAM = "degree"
        private const val STUDY_PLAN_URL_PARAM = "studyPlan"
        private const val FROM_DATE_URL_PARAM = "fromDate"
        private const val TO_DATE_URL_PARAM = "toDate"
        private const val PAGE_URL_PARAM = "page"

        const val DEFAULT_URL_PARAM_VALUE = "-1"

        const val BASE_TIMETABLE_URL = "$BASE_URL/$TIMETABLE_URL_PATH"
        const val BASE_CANTEEN_URL = "https://unibz.markas.info/menu"

        const val TIMETABLE_URL_LOCATED_REGEX = "(https://www.unibz.it/)..(/timetable)"
        const val TIMETABLE_URL_NOT_LOCATED_REGEX = "(https://www.unibz.it/timetable)"
        const val CANTEEN_URL_REGEX = "(https://unibz.markas.info/menu)"

        /**
         * Parses the url in order to get information about the selected study plan.
         */
        fun parseUrl(url: String): Map<UserPrefs.Pref, String>? {
            if (!validateUrl(url)) return null

            val uri = Uri.parse(url)

            return mapOf(
                UserPrefs.Pref.DEPARTMENT_ID to (uri.getQueryParameter(DEPARTMENT_URL_PARAM)
                    ?: DEFAULT_URL_PARAM_VALUE),
                UserPrefs.Pref.DEGREE_ID to (uri.getQueryParameter(DEGREE_URL_PARAM)
                    ?: DEFAULT_URL_PARAM_VALUE),
                UserPrefs.Pref.STUDY_PLAN_ID to (uri.getQueryParameter(STUDY_PLAN_URL_PARAM)
                    ?: DEFAULT_URL_PARAM_VALUE)
            )
        }

        private fun validateUrl(url: String) =
            StringUtils.isMatchingPartially(TIMETABLE_URL_NOT_LOCATED_REGEX, url)
                    || StringUtils.isMatchingPartially(TIMETABLE_URL_LOCATED_REGEX, url)
    }

    /**
     * Nested class which is used to build the [WebSiteUrl] for the remote timetable request.
     */
    data class Builder(
        var language: String = "en",
        var searchByKeywords: String = "",
        var department: String = "",
        var degree: String = "",
        var studyPlan: String = "",
        var fromDate: String = "",
        var toDate: String = "",
        var page: String = "1"
    ) {
        companion object {
            private const val NULL_VALUE = ""
        }

        private fun String.replaceDefaultUrlParam() = this.replace(DEFAULT_URL_PARAM_VALUE, "")

        private fun String.encodeSpaces() = this.replace(" ", "+")

        private fun String.encodeComma(): String {
            return if (this.contains(","))
                this.replace(",", "%2C")
            else
                this
        }

        private fun getTodayDate() = DateUtils.getCurrentDateFormatted()

        fun useDeviceLanguage() =
            apply { this.language = Locale.ENGLISH.language }

        fun withSearchKeywords(searchByKeywords: String?) =
            apply { this.searchByKeywords = searchByKeywords ?: NULL_VALUE }

        fun withLanguage(language: String?) = apply { this.language = language ?: NULL_VALUE }

        fun withDepartment(department: String?) =
            apply { this.department = department ?: NULL_VALUE }

        fun withDegree(degree: String?) = apply { this.degree = degree ?: NULL_VALUE }

        fun withStudyPlan(studyPlan: String?) = apply { this.studyPlan = studyPlan ?: NULL_VALUE }

        fun onlyToday() = apply {
            this.fromDate = getTodayDate()
            this.toDate = getTodayDate()
        }

        fun fromToday() = apply { this.fromDate = getTodayDate() }

        fun fromTomorrow() = apply { this.fromDate = DateUtils.getCurrentDatePlusDaysFormatted(1) }

        fun fromDate(fromDate: String?) = apply { this.fromDate = fromDate ?: NULL_VALUE }

        fun toNext7Days() = apply { this.toDate = DateUtils.getCurrentDatePlusDaysFormatted(7) }

        fun toOneYear() = apply { this.toDate = DateUtils.getCurrentDatePlusYearsFormatted(1) }

        fun toDate(toDate: String?) = apply { this.toDate = toDate ?: NULL_VALUE }

        fun atPage(page: String?) = apply { this.page = page ?: NULL_VALUE }

        fun build() = WebSiteUrl(
            BASE_URL +
                    "/$language" +
                    "/$TIMETABLE_URL_PATH" +
                    "/?$SEARCH_BY_KEYWORDS_URL_PARAM=${this.searchByKeywords.encodeSpaces()}" +
                    "&$DEPARTMENT_URL_PARAM=${this.department.replaceDefaultUrlParam().encodeComma()}" +
                    "&$DEGREE_URL_PARAM=${this.degree.replaceDefaultUrlParam().encodeComma()}" +
                    "&$STUDY_PLAN_URL_PARAM=${this.studyPlan.replaceDefaultUrlParam().encodeComma()}" +
                    "&$FROM_DATE_URL_PARAM=${this.fromDate}" +
                    "&$TO_DATE_URL_PARAM=${this.toDate}" +
                    "&$PAGE_URL_PARAM=${this.page}"
        )
    }

    override fun toString() = this.url
}