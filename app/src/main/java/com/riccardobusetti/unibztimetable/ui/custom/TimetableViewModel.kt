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
import kotlinx.coroutines.runBlocking

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
        ERROR_WHILE_GETTING_TIMETABLE(
            R.string.error_while_getting_timetable,
            R.drawable.ic_warning
        )
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

    /**
     * Data class representing the state of the list.
     *
     * @author Riccardo Busetti
     */
    data class ListState(
        var currentPage: Int = 0,
        var previousTotalItemCount: Int = 0,
        var loading: Boolean = true
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

    /**
     * Current page of the list that represents the actual page of the website from
     * where we query data.
     */
    var currentPage = DEFAULT_PAGE

    /**
     * State of the list which needs to be kept on configuration changes.
     *
     * The state is mutable in order to have a cleaner codebase, because we pass
     * the instance to the [EndlessRecyclerViewScrollListener] that changes the state
     * internally. This is beneficial because the view model holds the state so whenever
     * there is a configuration change the list will be reinitiated but it will have its
     * old state.
     */
    var listState = ListState()

    /**
     * Boolean stating if the list needs to be animated.
     *
     * In this case the animation will be only triggered on the app start.
     */
    var animateList = true

    /**
     * Converts a list of [Course] to a list of [DisplayableCourseGroup] which is the same entity
     * but displayable.
     */
    abstract fun coursesToCourseGroups(courses: List<Course>): List<DisplayableCourseGroup>

    fun resetListState() {
        listState.currentPage = 0
        listState.previousTotalItemCount = 0
        listState.loading = true
    }

    fun enableListAnimation() {
        animateList = true
    }

    fun disableListAnimation() {
        animateList = false
    }

    fun updateCurrentPage(newCurrentPage: String) {
        currentPage = newCurrentPage
    }

    fun requestTimetable(timetableRequest: TimetableRequest) {
        runBlocking {
            timetableRequests.send(timetableRequest)
        }
    }

    fun showLoading(isReset: Boolean = true) {
        if (isReset || isCurrentTimetableEmpty()) {
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
                _timetable.value = it

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

    /**
     * Joins the previous timetable with the new one in case we want to load a new page of the
     * same timetable.
     */
    private fun joinPrevAndNewTimetable(
        prevTimetable: List<DisplayableCourseGroup>,
        newTimetable: List<DisplayableCourseGroup>
    ): List<DisplayableCourseGroup> {
        val joinedList = mutableListOf<DisplayableCourseGroup>()

        joinedList.addAll(prevTimetable)
        joinedList.addAll(newTimetable)

        return joinedList
    }

    fun <T> Flow<T>.handleErrors(tag: String): Flow<T> =
        catch { e -> handleTimetableException(tag, e) }

    private fun handleTimetableException(tag: String, exception: Throwable) {
        Log.d(tag, "error while loading timetable: $exception -> ${exception.message}")
        showError(TimetableError.ERROR_WHILE_GETTING_TIMETABLE)
    }

    private fun isCurrentPageFirstPage() = currentPage == DEFAULT_PAGE

    /**
     * Checks if the view model is empty, in order to know if data needs to be loaded
     * or not.
     *
     * The view model is not cleared on app rotation whilst it is if the app is killed by
     * the OS when it is low on memory. So we load again the data if and only if the view model
     * has been cleared.
     */
    fun isViewModelEmpty() = _timetable.value == null

    fun isCurrentTimetableEmpty(): Boolean {
        _timetable.value?.let {
            return it.isEmpty()
        }

        return true
    }
}