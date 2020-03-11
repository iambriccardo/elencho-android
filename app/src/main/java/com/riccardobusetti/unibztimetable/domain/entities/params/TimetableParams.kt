package com.riccardobusetti.unibztimetable.domain.entities.params

class TimetableParams(
    val searchKeyword: String? = null,
    val department: String? = null,
    val degree: String? = null,
    val studyPlan: String? = null,
    val fromDate: String? = null,
    val toDate: String? = null,
    val page: String? = null
) : Params()