package com.riccardobusetti.unibztimetable.domain.repositories

import com.riccardobusetti.unibztimetable.data.remote.retrofit.BackendRetrofitClient
import com.riccardobusetti.unibztimetable.data.remote.retrofit.BackendService

/**
 * Repository implementation for the fetch of all the faculty courses available.
 *
 * @author Riccardo Busetti
 */
class ChooseFacultyRepository : Repository {

    // TODO: implement also local storage for the faculties.
    private val webservice: BackendService
        get() = BackendRetrofitClient.webservice

    suspend fun getDepartments() = webservice.getDepartments()

    suspend fun getDegrees() = webservice.getDegrees()

    suspend fun getDegrees(departmentId: String) = webservice.getDegrees(departmentId)

    suspend fun getStudyPlans() = webservice.getStudyPlans()

    suspend fun getStudyPlans(degreeId: String) = webservice.getStudyPlans(degreeId)
}