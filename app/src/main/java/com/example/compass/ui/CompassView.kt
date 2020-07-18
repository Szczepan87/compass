package com.example.compass.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.compass.R
import com.example.compass.util.AZIMUTH_DEFAULT_VALUE
import com.example.compass.util.HEADING_DEFAULT_VALUE
import kotlinx.android.synthetic.main.compass_view.view.*

class CompassView(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {

    private var heading = HEADING_DEFAULT_VALUE
    private var azimuth = AZIMUTH_DEFAULT_VALUE

    init {
        View.inflate(context, R.layout.compass_view, this)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CompassView)
        heading = attributes.getInt(R.styleable.CompassView_heading, HEADING_DEFAULT_VALUE)
        azimuth = attributes.getInt(R.styleable.CompassView_azimuth, AZIMUTH_DEFAULT_VALUE)
        compass_rose.rotation = heading.toFloat()
        azimuth_arrow.rotation = azimuth.toFloat()
        attributes.recycle()
    }

    fun setHeading(heading: Int) {
        this.heading = heading
        compass_rose.rotation = heading.toFloat()
        invalidate()
    }

    fun setAzimuth(azimuth: Int?) {
        if (azimuth == null) {
            azimuth_arrow.visibility = View.INVISIBLE
            return
        } else {
            this.azimuth = azimuth
            azimuth_arrow.visibility = View.VISIBLE
            azimuth_arrow.rotation = azimuth.toFloat()
            invalidate()
        }
    }
}