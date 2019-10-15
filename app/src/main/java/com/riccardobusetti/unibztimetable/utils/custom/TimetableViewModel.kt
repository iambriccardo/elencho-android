package com.riccardobusetti.unibztimetable.utils.custom

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Extension class of the [ViewModel] which provides the basics [MutableLiveData] objects that are
 * needed by the view to listen for events.
 *
 * @author Riccardo Busetti
 */
open class TimetableViewModel<TimetableType> : AdvancedViewModel() {

    /**
     * Error value if we don't have any error. Refer to the [error] documentation to understand
     * why this exists.
     */
    protected val NO_ERROR = ""

    /**
     * Live data object containing the timetable which has been loaded.
     */
    val timetable = MutableLiveData<TimetableType>()

    /**
     * Live data object containing the error which will be displayed. If empty we consider
     * that no error is present.
     *
     * The empty state is used as a signal for the observer to remove the error view.
     */
    val error = MutableLiveData<String>()

    /**
     * Live data object containing the current state of loading.
     */
    val loadingState = MutableLiveData<Boolean>()
}