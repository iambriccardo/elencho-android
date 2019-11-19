package com.riccardobusetti.unibztimetable.utils.exceptions

/**
 * Custom exception that will be thrown whenever there is no internet connection
 * during an operation that needs an internet connection.
 *
 * @author Riccardo Busetti
 */
class InternetNotAvailableException : Exception() {

    override val message: String?
        get() = "Internet is not available and it is needed for this operation to be performed."
}