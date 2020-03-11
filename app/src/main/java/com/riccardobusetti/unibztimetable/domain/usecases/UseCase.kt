package com.riccardobusetti.unibztimetable.domain.usecases

import com.riccardobusetti.unibztimetable.domain.entities.params.Params

/**
 * Interface which describes the use case which is responsible of running business logic
 * on data queried by the repository.
 *
 * Use cases are specific to a task, so in my architecture I will use different use cases based
 * on the type of operations they perform. This type of architecture creates a readable and
 * maintainable code, because the use case doesn't care about how to get data but it is only
 * responsible to manipulate it.
 *
 * @author Riccardo Busetti
 */
interface UseCase<P : Params?, T> {

    /**
     * Executes some operations and returns the result.
     */
    fun execute(params: P): T
}