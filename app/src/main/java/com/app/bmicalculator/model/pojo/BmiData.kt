package com.app.bmicalculator.model.pojo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BmiData(
    var id: Int = 0,
    var age: String = "",
    var height: String = "",
    var weight: String = "",
    var male: Boolean = false,
    var female: Boolean = false,
    var bmi: Float = 0f
) : Parcelable