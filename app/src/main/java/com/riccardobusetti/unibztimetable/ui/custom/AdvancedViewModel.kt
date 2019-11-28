package com.riccardobusetti.unibztimetable.ui.custom

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

/**
 * Extension class of [ViewModel] responsible of providing a common structure for all the viewmodels
 * used throughout the app.
 *
 * @author Riccardo Busetti
 */
open class AdvancedViewModel : ViewModel() {

    /**
     * Launches a coroutine and wraps it into the supervisorScope in order to avoid error propagation
     * of children to the parent.
     *
     * This is done because we want to have error checking inside of the coroutine to make the code
     * readable and easily mantainable.
     *
     * @author Riccardo Busetti
     */
    protected fun CoroutineScope.launchWithSupervisor(block: suspend CoroutineScope.() -> Unit) =
        this.launch { supervisorScope { block() } }
}