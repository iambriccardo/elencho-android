package com.riccardobusetti.unibztimetable.ui.custom

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.riccardobusetti.unibztimetable.domain.entities.app.AppSection
import java.io.Serializable

abstract class BaseFragment<ViewModel : BaseViewModel> : Fragment(), Serializable {

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
    abstract fun initViewModel(): ViewModel

    /**
     * Setups the user interface.
     *
     * In this method you should inflate ui elements and also modify them if necessary.
     */
    abstract fun setupUI()

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
     * Reloads the data when needed.
     *
     * In this method you put the code that needs to be triggered if you allow reloading in the
     * app section. The purpose of this method is to split up the two logics because with reload
     * you are supposed to perform a state reset of the data.
     */
    open fun reloadData() {}

    /**
     * This variable holds the [ViewModel] which is coupled with an [Activity] or [Fragment].
     * It is important to note that the model is only available after the [onCreate] method will
     * be called, thus it is nullable.
     */
    // TODO: find a way to replace this with [by viewModels {}].
    protected var model: ViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = initViewModel()
        model?.let {
            setupUI()
            attachObservers()
            it.start()
        }
    }
}