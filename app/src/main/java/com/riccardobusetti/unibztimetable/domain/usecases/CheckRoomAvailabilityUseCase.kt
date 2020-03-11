package com.riccardobusetti.unibztimetable.domain.usecases

import com.riccardobusetti.unibztimetable.data.remote.WebSiteUrl
import com.riccardobusetti.unibztimetable.domain.entities.AppSection
import com.riccardobusetti.unibztimetable.domain.entities.Course
import com.riccardobusetti.unibztimetable.domain.entities.params.TimetableParams
import com.riccardobusetti.unibztimetable.domain.repositories.TimetableRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take

/**
 * Use case which will manage the checking of room availability throughout all
 * the university.
 *
 * @author Riccardo Busetti
 */
class CheckRoomAvailabilityUseCase(
    private val timetableRepository: TimetableRepository
) : UseCase<TimetableParams, Flow<List<Course>>> {

    override fun execute(params: TimetableParams): Flow<List<Course>> {
        val webSiteUrl = WebSiteUrl.Builder()
            .useDeviceLanguage()
            .onlyToday()
            .withSearchKeywords(params.searchKeyword)
            .build()

        return timetableRepository.getTimetable(AppSection.ROOM_CHECK, webSiteUrl)
            .take(1)
            .map { courses ->
                courses.filter { !it.isFinished() }
            }
    }
}