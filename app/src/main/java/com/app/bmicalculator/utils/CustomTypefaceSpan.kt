package com.app.bmicalculator.utils

import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.TypefaceSpan

class CustomTypefaceSpan(family: String?, type: Typeface) : TypefaceSpan(family) {
    private val newType: Typeface

    init {
        newType = type
    }

    override fun updateDrawState(ds: TextPaint) {
        ds.typeface = newType
    }

    override fun updateMeasureState(paint: TextPaint) {
        paint.typeface = newType
    }
}