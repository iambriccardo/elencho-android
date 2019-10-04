package com.riccardobusetti.unibztimetable.domain.strategies

import com.riccardobusetti.unibztimetable.domain.entities.Day
import com.riccardobusetti.unibztimetable.network.WebSiteLink
import com.riccardobusetti.unibztimetable.network.WebSiteScraper

/**
 * Remote implementation for fetching the timetable.
 *
 * This implementation will scrape the unibz.it webpage
 * in order to obtain the timetable information.
 *
 * @author Riccardo Busetti
 */
class RemoteTimetableStrategy : TimetableStrategy {

    /**
     * @inheritDoc
     */
    override fun getTodayTimetable(
        department: String,
        degree: String,
        academicYear: String,
        page: String
    ): List<Day> {
        val webSiteLink = WebSiteLink.Builder()
            .useDeviceLanguage()
            .withDepartment(department)
            .withDegree(degree)
            .withAcademicYear(academicYear)
            .onlyToday()
            .atPage(page)
            .build()

        return WebSiteScraper(webSiteLink).getTimetable()
    }

    /**
     * @inheritDoc
     */
    override fun getNext7DaysTimetable(
        department: String,
        degree: String,
        academicYear: String,
        page: String
    ): List<Day> {
        val webSiteLink = WebSiteLink.Builder()
            .useDeviceLanguage()
            .withDepartment(department)
            .withDegree(degree)
            .withAcademicYear(academicYear)
            .fromToday()
            .toNext7Days()
            .atPage(page)
            .build()

        return WebSiteScraper(webSiteLink).getTimetable()
    }

    /**
     * @inheritDoc
     */
    override fun getTimetable(
        language: String,
        department: String,
        degree: String,
        academicYear: String,
        fromDate: String,
        toDate: String,
        page: String
    ): List<Day> {
        val webSiteLink = WebSiteLink.Builder()
            .withLanguage(language)
            .withDepartment(department)
            .withDegree(degree)
            .withAcademicYear(academicYear)
            .fromDate(fromDate)
            .toDate(toDate)
            .atPage(page)
            .build()

        return WebSiteScraper(webSiteLink).getTimetable()
    }

}