package com.riccardobusetti.unibztimetable.utils.custom.views

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.riccardobusetti.unibztimetable.R
import kotlinx.android.synthetic.main.compound_view_status.view.*

class StatusView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var statusImage: ImageView
    private lateinit var statusText: TextView

    init {
        inflate(getContext(), R.layout.compound_view_status, this)

        statusImage = compound_view_status_image
        statusText = compound_view_status_text
    }

    fun setText(text: String) {
        statusText.text = text
    }

    fun setImage() {

    }
}