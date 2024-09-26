package com.app.bmicalculator.model.pojo.auth.authentication

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserData(

    @SerializedName("country")
    var country: String? = null,

    @SerializedName("address")
    var address: String? = null,

    @SerializedName("city")
    var city: String? = null,

    @SerializedName("is_email_verified")
    var isEmailVerified: Boolean? = null,

    @SerializedName("profile_pic")
    var profilePic: String? = null,

    @SerializedName("created_at")
    var createdAt: String? = null,

    @SerializedName("created_by")
    var createdBy: String? = null,

    @SerializedName("deleted_at")
    var deletedAt: String? = null,

    @SerializedName("profile_image")
    var profileImage: String? = null,

    @SerializedName("country_code")
    var countryCode: String? = null,

    @SerializedName("user_role")
    var userRole: String? = null,

    @SerializedName("latitude")
    var latitude: Double? = null,

    @SerializedName("longitude")
    var longitude: Double? = null,

    @SerializedName("user_type")
    var userType: Int? = null,

    @SerializedName("category_id")
    var categoryId: String? = null,

    @SerializedName("updated_at")
    var updatedAt: String? = null,

    @SerializedName("company_name", alternate = ["companyName"])
    var companyName: String? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("updated_by")
    var updatedBy: String? = null,

    @SerializedName("phone_number")
    var phoneNumber: String? = null,

    @SerializedName("_id")
    var userId: String? = null,

    @SerializedName("id", alternate = ["uuid"])
    var uuid: String? = null,

    @SerializedName("postal_code")
    var postalCode: String? = null,

    @SerializedName("email")
    var email: String? = null,

    @SerializedName("status")
    var status: Int? = null,

    @SerializedName("Expiry_time", alternate = ["expiry_time"])
    val expiryTime: Long? = null,

    @SerializedName("token", alternate = ["verificationOtp_id"])
    val token: String? = null,

    @SerializedName("imageUrl")
    val imageUrl: List<String?>? = ArrayList()
) : Parcelable