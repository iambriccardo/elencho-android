package com.riccardobusetti.unibztimetable.domain.strategies

import com.riccardobusetti.unibztimetable.data.remote.WebSiteScraper
import com.riccardobusetti.unibztimetable.data.remote.WebSiteUrl
import com.riccardobusetti.unibztimetable.domain.entities.Kourse

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
    override fun getTimetable(webSiteUrl: WebSiteUrl): List<Kourse> =
        WebSiteScraper(webSiteUrl).getTimetable()
}