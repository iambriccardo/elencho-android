package com.riccardobusetti.unibztimetable.ui.custom

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Extension class of [ViewModel] responsible of providing a common structure for all the viewmodels
 * used throughout the app.
 *
 * @author Riccardo Busetti
 */
abstract class BaseViewModel : ViewModel() {

    companion object {
        private const val NO_TAG = "Untagged"
    }

    var isFirstLaunch = true

    protected fun CoroutineScope.safeLaunch(
        tag: String? = NO_TAG,
        block: suspend CoroutineScope.() -> Unit
    ) = safeLaunch(tag, block, null)

    protected fun CoroutineScope.safeLaunch(
        tag: String? = NO_TAG,
        block: suspend CoroutineScope.() -> Unit,
        onError: ((Throwable) -> Unit)?
    ) =
        this.launch(catchError(tag, onError)) {
            block()
        }

    private fun catchError(tag: String?, onError: ((Throwable) -> Unit)? = null) =
        CoroutineExceptionHandler { _, throwable ->
            Log.d(tag, "an error occurred in the viewmodel: $throwable")
            onError?.let { it(throwable) }
        }

    /**
     * Starts the view model and set ups everything needed for it to properly work.
     */
    abstract fun start()
}