package com.app.bmicalculator.network.retrofit

sealed class DataResult<out T> {
    data class Loading(val nothing: Nothing? = null) : DataResult<Nothing>()
    data class Success<out T>(val data: T? = null, val code: Int = 0) : DataResult<T?>()
    data class Failure(
        val message: String? = null,
        val code: Int = 0,
        val exception: Exception? = null
    ) :
        DataResult<Nothing>()

    data class Logout(val code: Int = 0, val exception: Exception? = null) : DataResult<Nothing>()


}