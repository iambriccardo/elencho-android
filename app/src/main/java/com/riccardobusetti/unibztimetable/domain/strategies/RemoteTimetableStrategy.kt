package com.riccardobusetti.unibztimetable.domain.strategies

import com.riccardobusetti.unibztimetable.data.remote.WebSiteScraper
import com.riccardobusetti.unibztimetable.data.remote.WebSiteUrl
import com.riccardobusetti.unibztimetable.domain.entities.Course

/**
 * Remote implementation for fetching the timetable.
 *
 * This implementation will scrape the unibz.it webpage
 * in order to obtain the timetable information.
 *
 * @author Riccardo Busetti
 */
class RemoteTimetableStrategy : TimetableStrategy<WebSiteUrl> {

    /**
     * @inheritDoc
     */
    override fun getTimetable(query: WebSiteUrl): List<Course> =
        WebSiteScraper(query).getTimetable()
}