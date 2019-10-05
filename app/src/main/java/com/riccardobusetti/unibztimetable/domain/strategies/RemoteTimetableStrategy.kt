package com.riccardobusetti.unibztimetable.domain.strategies

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
    override fun getTimetable(webSiteLink: WebSiteLink) = WebSiteScraper(webSiteLink).getTimetable()
}