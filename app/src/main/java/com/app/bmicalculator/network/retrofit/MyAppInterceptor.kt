package com.app.bmicalculator.network.retrofit


import com.app.bmicalculator.App.Companion.appContext
import com.app.bmicalculator.utils.Const
import com.app.bmicalculator.utils.Prefs
import com.app.bmicalculator.utils.extensions.log
import okhttp3.Interceptor
import okhttp3.Response


class MyAppInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        log("token ${Prefs.with(appContext)!!.getString(Const.USER_TOKEN, "")}")
        var request = chain.request()
        val headers = request.headers.newBuilder()
            .add("Content-Type", "application/json")
            .add("Accept", "application/json")
            .add(
                "Authorization",
                "Bearer ${Prefs.with(appContext)!!.getString(Const.USER_TOKEN, "")}"
            )
            .build()

        request = request.newBuilder().headers(headers).build()

        return chain.proceed(request)
    }
}