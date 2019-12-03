package com.riccardobusetti.unibztimetable.ui.custom

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.riccardobusetti.unibztimetable.domain.entities.AppSection
import com.riccardobusetti.unibztimetable.domain.entities.DisplayableCourseGroup
import com.riccardobusetti.unibztimetable.ui.items.CourseGroupItem
import com.riccardobusetti.unibztimetable.ui.items.CourseItem
import com.riccardobusetti.unibztimetable.ui.items.OngoingCourseItem
import com.riccardobusetti.unibztimetable.utils.custom.EndlessRecyclerViewScrollListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section

/**
 * Extension of the [Fragment] class which enhances its functionality by creating a standardized
 * structure for all the fragments which are using the view models and live data.
 *
 * @author Riccardo Busetti
 */
abstract class AdvancedFragment<ViewModel : TimetableViewModel> : Fragment() {

    protected lateinit var scrollListener: EndlessRecyclerViewScrollListener

    /**
     * [RecyclerView] extension function which will handle the endless scroll of the list, by
     * calling a specific function whenever the end has been reached.
     */
    fun RecyclerView.onEndReached(block: (String) -> Unit): EndlessRecyclerViewScrollListener {
        val scrollListener =
            EndlessRecyclerViewScrollListener(this.layoutManager as LinearLayoutManager) {
                block("${it + 1}")
            }

        this.addOnScrollListener(scrollListener)

        return scrollListener
    }

    /**
     * This variable holds the [ViewModel] which is coupled with an [Activity] or [Fragment].
     * It is important to note that the model is only available after the [onCreate] method will
     * be called, thus it is nullable.
     */
    protected var model: ViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = initModel()

        setupUi()

        attachObservers()

        model?.let {
            if (it.isViewModelEmpty()) {
                loadData()
            }
        }
    }

    /**
     * Variables that holds the section of the app that inherits this advanced fragment behavior.
     */
    abstract val appSection: AppSection

    /**
     * Initializes the view model inside of the onCreate method in order to make it available
     * as soon as the fragment is being instantiated.
     *
     * In this method you should get the view model from the provider.
     */
    abstract fun initModel(): ViewModel

    /**
     * Setups the user interface.
     *
     * In this method you should inflate ui elements and also modify them if necessary.
     */
    abstract fun setupUi()

    /**
     * Attaches the observers to the live data objects which are going to notify them
     * for every change.
     *
     * In this method you should observe all the live data objects of the view model.
     */
    abstract fun attachObservers()

    /**
     * Loads data that is needed in the fragment.
     *
     * In this method you should put all the methods which are loadingState data which is needed at
     * the start. This method is only called at the creation of the view, so if you need to
     * change its behavior just call it in every lifecycle method you want.
     */
    open fun loadData() {}

    /**
     * Adds the timetable to a specific adapter.
     * This method doesn't remove the existing items, so the logic must be handled separately.
     */
    @TargetApi(Build.VERSION_CODES.O)
    fun GroupAdapter<GroupieViewHolder>.addTimetable(courseGroups: List<DisplayableCourseGroup>) {
        courseGroups.forEach { header ->
            val section = Section()
            section.setHeader(CourseGroupItem(header))

            header.courses.forEach { course ->
                section.add(
                    if (appSection == AppSection.TODAY && course.isOngoing) OngoingCourseItem(
                        course
                    ) else CourseItem(course)
                )
            }

            add(section)
        }
    }


    /**
     * Clears the list and adds the new timetable.
     */
    fun GroupAdapter<GroupieViewHolder>.clearAndAddTimetable(courseGroups: List<DisplayableCourseGroup>) {
        clear()
        addTimetable(courseGroups)
    }
}