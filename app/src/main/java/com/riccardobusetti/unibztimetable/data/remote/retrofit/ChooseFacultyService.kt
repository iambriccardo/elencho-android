package com.riccardobusetti.unibztimetable.data.remote.retrofit

import com.riccardobusetti.unibztimetable.domain.entities.Degree
import com.riccardobusetti.unibztimetable.domain.entities.Department
import com.riccardobusetti.unibztimetable.domain.entities.StudyPlan
import retrofit2.http.GET
import retrofit2.http.Query

interface ChooseFacultyService {
    @GET("departments")
    suspend fun getDepartments(): List<Department>

    @GET("degrees")
    suspend fun getDegrees(): List<Degree>

    @GET("degrees")
    suspend fun getDegrees(@Query("departmentId") departmentId: String): List<Degree>

    @GET("studyPlans")
    suspend fun getStudyPlans(): List<StudyPlan>

    @GET("studyPlans")
    suspend fun getStudyPlans(@Query("degreeId") degreeId: String): List<StudyPlan>
}