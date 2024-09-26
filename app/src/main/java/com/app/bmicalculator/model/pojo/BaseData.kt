package com.app.bmicalculator.model.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BaseData(

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("profile_pic", alternate = ["image"])
    val profilePic: String? = null,

    @SerializedName("_id")
    val id: String? = null
) : Parcelable

