package com.app.bmicalculator.viewModels.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.app.bmicalculator.model.pojo.auth.authentication.UserData
import com.app.bmicalculator.model.repository.local.UserRepository
import com.app.bmicalculator.model.repository.remote.AuthorizationRepository
import com.app.bmicalculator.network.retrofit.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BaseViewModel @Inject constructor(
    private val mApplication: Application,
    val apiService: ApiService,
    private val dataRepository: AuthorizationRepository,
    private val userRepository: UserRepository
) : AndroidViewModel(mApplication) {

    var isRefresh: Boolean = false

    fun saveUserData(data: UserData?) {
        userRepository.saveUserData(data)

    }

    fun saveToken(token: String?) {
        userRepository.saveToken(token)

    }

    fun getToken(): String? {
        return userRepository.getToken()

    }

    fun getUserData(): UserData? {
        return userRepository.getUser()

    }


}


