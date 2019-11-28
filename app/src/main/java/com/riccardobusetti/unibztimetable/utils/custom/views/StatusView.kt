package com.riccardobusetti.unibztimetable.utils.custom.views

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.ui.custom.TimetableViewModel
import kotlinx.android.synthetic.main.compound_view_status.view.*

/**
 * Compound view which will be responsible of showing to the user a status view. This view
 * can contain any kind of state.
 *
 * @author Riccardo Busetti
 */
class StatusView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var statusImage: ImageView
    private var statusText: TextView

    init {
        inflate(getContext(), R.layout.compound_view_status, this)

        statusImage = compound_view_status_image
        statusText = compound_view_status_text
    }

    fun setError(error: TimetableViewModel.TimetableError) {
        statusText.text = context.getString(error.descriptionResId)
        statusImage.setImageResource(error.imageResId)
    }

    fun setText(text: String) {
        statusText.text = text
    }

    fun setImage(@IdRes @DrawableRes imageResId: Int) {
        statusImage.setImageResource(imageResId)
    }
}