package com.riccardobusetti.unibztimetable.utils.custom

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riccardobusetti.unibztimetable.R

/**
 * Extension class of the [ViewModel] which provides the basics [MutableLiveData] objects that are
 * needed by the view to listen for events.
 *
 * @author Riccardo Busetti
 */
abstract class TimetableViewModel<TimetableType> : AdvancedViewModel() {

    /* TODO: find a way to avoid code duplication in the childs of this class */

    /**
     * Enum class representing all the errors which can occur while getting the timetable.
     *
     * @author Riccardo Busetti
     */
    enum class TimetableError(
        @IdRes @StringRes val descriptionResId: Int,
        @IdRes @DrawableRes val imageResId: Int
    ) {
        EMPTY_TIMETABLE(R.string.empty_timetable, R.drawable.ic_happy),
        ERROR_WHILE_GETTING_TIMETABLE(R.string.error_while_getting_timetable, R.drawable.ic_warning)
    }

    /**
     * Enum class representing all the loading states of the timetable fetching.
     *
     * We distinguish the loading states because in the UI we use different loading components
     * to show different states.
     *
     * @author Riccardo Busetti
     */
    enum class TimetableLoadingState {
        LOADING_FROM_SCRATCH,
        LOADING_WITH_DATA,
        NOT_LOADING
    }

    companion object {
        const val DEFAULT_PAGE = "1"
    }

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
    val error = MutableLiveData<TimetableError?>()

    /**
     * Live data object containing the current state of loading.
     */
    val loadingState = MutableLiveData<TimetableLoadingState>()

    /**
     * Live data object containing the current page.
     */
    val currentPage = MutableLiveData<String>().apply { this.value = DEFAULT_PAGE }

    fun isCurrentPageFirstPage() = currentPage.value == DEFAULT_PAGE

    /**
     * Checks if the current timetable is empty.
     */
    abstract fun isCurrentTimetableEmpty(): Boolean
}