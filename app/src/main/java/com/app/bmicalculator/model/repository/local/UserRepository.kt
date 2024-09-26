package com.app.bmicalculator.model.repository.local


import com.app.bmicalculator.App
import com.app.bmicalculator.model.pojo.auth.authentication.UserData
import com.app.bmicalculator.network.retrofit.ApiService
import com.app.bmicalculator.utils.Const
import com.app.bmicalculator.utils.Prefs
import javax.inject.Inject

class UserRepository @Inject constructor(private val apiService: ApiService) {
    fun saveToken(token: String?) {
        Prefs.with(App.appContext)!!.save(Const.USER_TOKEN, token)
    }


    fun saveUserData(userData: UserData?) {
        Prefs.with(App.appContext)!!
            .save(Const.USER_DETAILS, userData ?: UserData())
    }

    fun getUser(): UserData? {
        return Prefs.with(App.appContext)!!
            .getObject(Const.USER_DETAILS, UserData::class.java)
    }

    fun getToken() =
        Prefs.with(App.appContext)!!.getString(Const.USER_TOKEN, "")

    fun clearData() {
        Prefs.with(App.appContext)!!.removeAll()
    }
}