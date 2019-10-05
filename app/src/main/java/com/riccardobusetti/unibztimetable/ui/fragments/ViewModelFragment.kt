package com.riccardobusetti.unibztimetable.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * Extension of the [Fragment] class which simplifies the management of viewmodels
 * inside of the fragment.
 *
 * @author Riccardo Busetti
 */
abstract class ViewModelFragment<ViewModel> : Fragment() {

    protected var model: ViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = initModel()
    }

    /**
     * Initializes the view model inside of the onCreate method in order to make it available
     * as soon as the fragment is being instantiated.
     */
    abstract fun initModel(): ViewModel
}