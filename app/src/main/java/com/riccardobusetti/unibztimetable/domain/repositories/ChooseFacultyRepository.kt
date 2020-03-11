package com.riccardobusetti.unibztimetable.domain.repositories

import com.riccardobusetti.unibztimetable.data.remote.retrofit.ChooseFacultyRetrofitClient
import com.riccardobusetti.unibztimetable.data.remote.retrofit.ChooseFacultyService

/**
 * Repository implementation for the fetch of all the faculty courses available.
 *
 * @author Riccardo Busetti
 */
class ChooseFacultyRepository : Repository {

    private val webservice: ChooseFacultyService
        get() = ChooseFacultyRetrofitClient.webservice

    suspend fun getDepartments() = webservice.getDepartments()

    suspend fun getDegrees() = webservice.getDegrees()

    suspend fun getDegrees(departmentId: String) = webservice.getDegrees(departmentId)

    suspend fun getStudyPlans() = webservice.getStudyPlans()

    suspend fun getStudyPlans(degreeId: String) = webservice.getStudyPlans(degreeId)
}