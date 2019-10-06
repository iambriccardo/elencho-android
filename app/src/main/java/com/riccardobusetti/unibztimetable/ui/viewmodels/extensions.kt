package com.riccardobusetti.unibztimetable.ui.viewmodels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

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