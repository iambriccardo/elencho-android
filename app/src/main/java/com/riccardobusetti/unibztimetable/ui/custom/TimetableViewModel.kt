package com.riccardobusetti.unibztimetable.ui.custom

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.Course
import com.riccardobusetti.unibztimetable.domain.entities.DisplayableCourseGroup
import kotlinx.coroutines.channels.Channel
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

    /**
     * Data class representing the request for a new timetable.
     *
     * @author Riccardo Busetti
     */
    data class TimetableRequest(
        val fromDate: String? = null,
        val toDate: String? = null,
        val page: String,
        val isReset: Boolean
    )

    companion object {
        const val DEFAULT_PAGE = "1"
    }

    /**
     * Live data object containing the timetable which has been loaded.
     */
    private val _timetable = MutableLiveData<List<DisplayableCourseGroup>>()
    val timetable: LiveData<List<DisplayableCourseGroup>>
        get() = _timetable

    /**
     * Live data object containing the current page.
     */
    private val _currentPage = MutableLiveData<String>().apply {
        this.value =
            DEFAULT_PAGE
    }
    val currentPage: LiveData<String>
        get() = _currentPage

    /**
     * Live data object containing the error which will be displayed. If empty we consider
     * that no error is present.
     *
     * The empty state is used as a signal for the observer to remove the error view.
     */
    private val _error = MutableLiveData<TimetableError?>()
    val error: LiveData<TimetableError?>
        get() = _error

    /**
     * Live data object containing the current state of loading.
     */
    private val _loadingState = MutableLiveData<TimetableLoadingState>()
    val loadingState: LiveData<TimetableLoadingState>
        get() = _loadingState

    /**
     * Channel that will produce [TimetableRequest] which are sent by the UI and produced
     * by the [ViewModel] in order to sequentially process requests for the timetable.
     *
     * This is done because of paging, so we will load one page after the other in order
     * to avoid receiving a greater page before a lower one, resulting in an inconsistent
     * list.
     */
    val timetableRequests = Channel<TimetableRequest>()

    abstract fun coursesToCourseGroups(courses: List<Course>): List<DisplayableCourseGroup>

    fun showLoading() {
        if (isCurrentTimetableEmpty()) {
            _loadingState.value =
                TimetableLoadingState.LOADING_FROM_SCRATCH
        } else {
            _loadingState.value =
                TimetableLoadingState.LOADING_WITH_DATA
        }
    }

    fun hideLoading() {
        _loadingState.value =
            TimetableLoadingState.NOT_LOADING
    }

    fun showError(timetableError: TimetableError) {
        _error.value = timetableError
    }

    fun hideError() {
        _error.value = null
    }

    fun showTimetable(courses: List<Course>?, isReset: Boolean) {
        coursesToCourseGroups(courses!!).let {
            if (it.isEmpty() && isCurrentPageFirstPage()) {
                showError(TimetableError.EMPTY_TIMETABLE)
            } else if (it.isNotEmpty()) {
                hideError()

                if (isReset) {
                    _timetable.value = it
                } else {
                    _timetable.value = joinPrevAndNewTimetable(
                        _timetable.value ?: emptyList(),
                        it
                    )
                }

            }
        }
    }

    private fun joinPrevAndNewTimetable(
        prevTimetable: List<DisplayableCourseGroup>,
        newTimetable: List<DisplayableCourseGroup>
    ): List<DisplayableCourseGroup> {
        val joinedList = mutableListOf<DisplayableCourseGroup>()

        joinedList.addAll(prevTimetable.map {
            DisplayableCourseGroup(
                title = it.title,
                isNow = it.isNow,
                courses = it.courses,
                isAppendable = false
            )
        })
        joinedList.addAll(newTimetable)

        return joinedList
    }

    fun <T> Flow<T>.handleErrors(tag: String): Flow<T> =
        catch { e -> handleTimetableException(tag, e.message) }

    private fun handleTimetableException(tag: String, message: String?) {
        Log.d(tag, "Error while loading the timetable: $message")
        showError(TimetableError.ERROR_WHILE_GETTING_TIMETABLE)
    }

    fun updateCurrentPage(newCurrentPage: String) {
        _currentPage.value = newCurrentPage
    }

    fun isCurrentPageFirstPage() = currentPage.value == DEFAULT_PAGE

    /**
     * Checks if the view model is empty, in order to know if data needs to be loaded
     * or not.
     *
     * The view model is not cleared on app rotation whilst it is if the app is killed by
     * the OS when it is low on memory. So we load again the data if and only if the view model
     * has been cleared.
     */
    fun isViewModelEmpty() = timetable.value == null

    private fun isCurrentTimetableEmpty(): Boolean {
        timetable.value?.let {
            return it.isEmpty()
        }

        return false
    }
}