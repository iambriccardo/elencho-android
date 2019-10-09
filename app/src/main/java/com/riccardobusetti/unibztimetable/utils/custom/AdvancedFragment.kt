package com.riccardobusetti.unibztimetable.utils.custom

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/**
 * Extension of the [Fragment] class which enhances its functionality by creating a standardized
 * structure for all the fragments which are using the view models and live data.
 *
 * @author Riccardo Busetti
 */
abstract class AdvancedFragment<ViewModel> : Fragment() {

    companion object {
        private const val IS_DEVICE_ROTATED_KEY = "IS_DEVICE_ROTATED"
    }

    protected var model: ViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = initModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        attachObservers()
        if (savedInstanceState == null) startLoadingData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean(IS_DEVICE_ROTATED_KEY, true)
    }

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
     * Starts the loadingState of data.
     *
     * In this method you should put all the methods which are loadingState data which is needed at
     * the start. This method is only called at the creation of the view, so if you need to
     * change its behavior just call it in every lifecycle method you want.
     */
    abstract fun startLoadingData()
}