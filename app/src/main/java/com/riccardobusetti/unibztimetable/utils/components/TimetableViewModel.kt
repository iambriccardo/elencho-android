package com.riccardobusetti.unibztimetable.utils.components

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

/**
 * Extension class of the [ViewModel] which provides the basics [MutableLiveData] objects that are
 * needed by the view to listen for events.
 *
 * @author Riccardo Busetti
 */
open class TimetableViewModel<TimetableType> : ViewModel() {

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

    /**
     * Launches a coroutine and wraps it into the supervisorScope in order to avoid error propagation
     * of childs to the parent.
     *
     * This is done because we want to have error checking inside of the coroutine to make the code
     * readable and easily mantainable.
     *
     * @author Riccardo Busetti
     */
    fun CoroutineScope.launchWithSupervisor(block: suspend CoroutineScope.() -> Unit) =
        this.launch { supervisorScope { block() } }
}