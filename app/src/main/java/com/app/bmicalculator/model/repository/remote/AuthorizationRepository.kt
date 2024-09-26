package com.app.bmicalculator.model.repository.remote


import com.app.bmicalculator.network.retrofit.ApiService
import javax.inject.Inject


class AuthorizationRepository @Inject constructor(private val apiService: ApiService) {

    /* suspend fun apiValidateEmail(jsonObject: JsonObject?): Flow<DataResult<AuthenticationResponseData?>> {
         return object :
             NetworkOnlineDataRepo<AuthenticationResponseData, AuthenticationResponseData>() {
             override suspend fun fetchDataFromRemoteSource(): Response<AuthenticationResponseData> {
                 return apiService.apiValidateEmail(jsonObject)
             }
         }.asFlow().flowOn(Dispatchers.IO)
     }*/


}



