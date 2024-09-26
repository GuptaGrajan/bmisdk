package com.app.bmicalculator.model.pojo.auth

import android.graphics.drawable.Drawable
import java.io.Serializable

data class DashBoardModel(
    var id: Int = 0,
    var stageId: String = "",
    var description: String = "",
    var title: String = "",
    var value: String = "",
    var rootBG: Int = 0,
    var icon: Int = 0,
    var color: Int = 0,
    var score: String = "0",
    var drawable: Drawable? = null,
    var isSelected: Boolean = false,
    var isTitle: Boolean = false,
    var showScore: Boolean = false
) : Serializable