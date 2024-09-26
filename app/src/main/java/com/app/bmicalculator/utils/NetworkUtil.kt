package com.app.bmicalculator.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.app.bmicalculator.R


object NetworkUtil {

    var TYPE_WIFI = 1
    var TYPE_MOBILE = 2
    var TYPE_NOT_CONNECTED = 0
    var TYPE_IS_CONNECTING = 3

    fun getConnectivityStatus(context: Context): Int {
        val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = cm.activeNetworkInfo
        if (null != activeNetwork) {
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI

            if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE

            val info = cm.allNetworkInfo

            for (i in info.indices) {
                if (info[i].detailedState == NetworkInfo.DetailedState.CONNECTING) {
                    return TYPE_IS_CONNECTING
                }
            }
        }

        return TYPE_NOT_CONNECTED
    }

    fun getConnectivityStatusString(context: Context): String? {
        val conn = getConnectivityStatus(context)
        var status: String? = null
        if (conn == TYPE_NOT_CONNECTED) {
            status = context.getString(R.string.not_connected_to_internet)
        } else if (conn == TYPE_IS_CONNECTING) {
            status = context.getString(R.string.poor_internet_connection)
        }
        return status
    }
}
