package com.riccardobusetti.unibztimetable.ui.custom

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.Course
import com.riccardobusetti.unibztimetable.domain.entities.DisplayableCourseGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

/**
 * Extension class of the [ViewModel] which provides the basics [MutableLiveData] objects that are
 * needed by the view to listen for events.
 *
 * @author Riccardo Busetti
 */
abstract class TimetableViewModel : AdvancedViewModel() {

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
    val timetable = MutableLiveData<List<DisplayableCourseGroup>>()

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
    val currentPage = MutableLiveData<String>().apply {
        this.value =
            DEFAULT_PAGE
    }

    abstract fun coursesToCourseGroups(courses: List<Course>): List<DisplayableCourseGroup>

    fun showLoading() {
        if (isCurrentTimetableEmpty()) {
            loadingState.value =
                TimetableLoadingState.LOADING_FROM_SCRATCH
        } else {
            loadingState.value =
                TimetableLoadingState.LOADING_WITH_DATA
        }
    }

    fun hideLoading() {
        loadingState.value =
            TimetableLoadingState.NOT_LOADING
    }

    fun showError(error: TimetableError) {
        this.error.value = error
    }

    fun hideError() {
        error.value = null
    }

    fun showTimetable(courses: List<Course>?) {
        coursesToCourseGroups(courses!!).let {
            if (it.isEmpty() && isCurrentPageFirstPage()) {
                showError(TimetableError.EMPTY_TIMETABLE)
            } else {
                hideError()
                this.timetable.value = it
            }
        }
    }

    fun <T> Flow<T>.handleErrors(tag: String): Flow<T> =
        catch { e -> handleTimetableException(tag, e.message) }

    private fun handleTimetableException(tag: String, message: String?) {
        Log.d(tag, "Error while loading the timetable: $message")
        showError(TimetableError.ERROR_WHILE_GETTING_TIMETABLE)
    }

    fun isCurrentPageFirstPage() = currentPage.value == DEFAULT_PAGE

    private fun isCurrentTimetableEmpty(): Boolean {
        timetable.value?.let {
            return it.isEmpty()
        }

        return false
    }
}