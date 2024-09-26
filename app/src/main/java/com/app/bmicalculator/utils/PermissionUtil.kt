package com.app.bmicalculator.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat


class PermissionUtil {

    private var onPermissionChanged: ((permission: PERMISSION, isAvailable: Boolean) -> Unit)? =
        null

    private fun isPermissionAvailable(context: Context, permission: PERMISSION): Boolean {
        permission.permission.forEach {
            if ((ContextCompat.checkSelfPermission(
                    context,
                    it
                ) != PackageManager.PERMISSION_GRANTED)
            ) {
                return false
            }
        }
        return true
    }


    fun checkPermission(activity: Activity, permission: PERMISSION) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (isPermissionAvailable(activity, permission)) {
                onPermissionChanged?.invoke(permission, true)
            } else {
                activity.requestPermissions(permission.permission, permission.requestCode)
            }
        } else {
            onPermissionChanged?.invoke(permission, true)
        }
    }


    fun onPermissionGranted(activity: Activity, requestCode: Int) {

        val permission = when (requestCode) {

            PERMISSION.COARSE_LOCATION.requestCode -> {
                PERMISSION.COARSE_LOCATION
            }

            PERMISSION.FINE_LOCATION.requestCode -> {
                PERMISSION.FINE_LOCATION
            }

            PERMISSION.CAMERA_AND_STORAGE_PERMISSION.requestCode -> {
                PERMISSION.CAMERA_AND_STORAGE_PERMISSION
            }

            PERMISSION.LOCATION.requestCode -> {
                PERMISSION.LOCATION
            }

            else -> null
        }

        permission?.let {
            if (isPermissionAvailable(activity, permission)) {
                onPermissionChanged?.invoke(permission, true)
            } else {
                onPermissionChanged?.invoke(permission, false)
            }
        }
    }
}

enum class PERMISSION(val permission: Array<String>, val requestCode: Int) {
    FINE_LOCATION(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101),
    COARSE_LOCATION(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 102),
    WRITE_EXTERNAL_STORAGE(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 103),
    READ_EXTERNAL_STORAGE(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 104),
    CAMERA_PERMISSION(arrayOf(Manifest.permission.CAMERA), 105),
    CAMERA_AND_STORAGE_PERMISSION(
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ), 106
    ),
    LOCATION(
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ), 107
    )


}


