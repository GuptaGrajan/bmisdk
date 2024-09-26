package com.app.bmicalculator.model.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BaseResponseData(

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("status")
    val status: Int? = null,

    @SerializedName("statusCode")
    val statusCode: Int? = null,


    @SerializedName("data")
    val data: BaseData? = BaseData()


) : Parcelable