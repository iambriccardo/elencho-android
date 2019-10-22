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
open class TimetableViewModel<TimetableType> : AdvancedViewModel() {

    /**
     * Enum class representing all the possible errors which can occur while getting the timetable.
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
    val loadingState = MutableLiveData<Boolean>()

    /**
     * Live data object containing the current page.
     */
    val currentPage = MutableLiveData<String>().apply { this.value = "1" }
}