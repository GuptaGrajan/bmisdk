package com.app.bmicalculator.utils

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object Utils {
    val isDeviceRooted: Boolean
        get() = checkRootMethod1() || checkRootMethod2() || checkRootMethod3()

    private fun checkRootMethod1(): Boolean {
        val buildTags = Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }

    private fun checkRootMethod2(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su"
        )
        for (path in paths) {
            if (File(path).exists()) return true
        }
        return false
    }

    private fun checkRootMethod3(): Boolean {
        var process: Process? = null
        return try {
            process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            val `in` = BufferedReader(InputStreamReader(process.inputStream))
            `in`.readLine() != null
        } catch (t: Throwable) {
            false
        } finally {
            process?.destroy()
        }
    }


    fun getDeviceName(): String {
        return Build.MANUFACTURER + Build.MODEL
    }

    fun getAppVersion(context: Context): Int {
        var pInfo: PackageInfo? = null
        return try {
            pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            pInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            0
        }
    }

    fun getBatteryPercentage(context: Context): Float {
        return try {
            val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryStatus = context.registerReceiver(null, ifilter)
            val level = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val batteryPct = level / scale.toFloat() * 100

            // Are we charging / charged?
            val status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL
            if (isCharging) {
                70f
            } else {
                batteryPct.toFloat()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            70f
        }
    }

    fun getActualBatteryPer(context: Context): String? {
        return try {
            val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryStatus = context.registerReceiver(null, ifilter)
            val level = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val batteryPct = level / scale.toFloat() * 100

            // Are we charging / charged?
            val status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL
            batteryPct.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            "0"
        }
    }

    fun isBatteryChargingNew(context: Context): Int {
        return try {
            val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryStatus = context.registerReceiver(null, ifilter)
            val level = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val batteryPct = level / scale.toFloat() * 100
            // Are we charging / charged?
            val status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL
            if (isCharging) {
                1
            } else {
                0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    fun isBatteryCharging(context: Context): Boolean {
        return try {
            val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryStatus = context.registerReceiver(null, ifilter)
            val level = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val batteryPct = level / scale.toFloat() * 100
            // Are we charging / charged?
            val status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL || batteryPct > 60
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    fun deleteGpsData(context: Context) {
        /* Cold start */
        val bundle = Bundle()
        bundle.putBoolean("all", true)
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.sendExtraCommand(LocationManager.GPS_PROVIDER, "delete_aiding_data", null)
        locationManager.sendExtraCommand("gps", "force_xtra_injection", bundle)
        locationManager.sendExtraCommand("gps", "force_time_injection", bundle)
    }


    fun isAppInstalled(packagename: String?, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packagename!!, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }


    fun mockLocationEnabled(location: Location?): Boolean {
        return location?.isFromMockProvider
            ?: false
    }


    fun compareDouble(d1: Double, d2: Double): Int {
        return if (d1 == d2) {
            0
        } else {
            val epsilon = 0.0000001
            if (d1 - d2 > epsilon) {
                1
            } else if (d1 - d2 < epsilon) {
                -1
            } else {
                0
            }
        }
    }


    fun getChronoTimeFromMillis(elapsedTime: Long): String {
        val hR = (elapsedTime / 3600000).toInt()
        val mR = (elapsedTime - hR * 3600000).toInt() / 60000
        val sR = (elapsedTime - hR * 3600000 - mR * 60000).toInt() / 1000
        val hhR = if (hR < 10) "0$hR" else hR.toString() + ""
        val mmR = if (mR < 10) "0$mR" else mR.toString() + ""
        val ssR = if (sR < 10) "0$sR" else sR.toString() + ""
        return "$hhR:$mmR:$ssR"
    }


    fun telerickshawInstall(context: Context): Boolean {

        // Flags: See below
        var telerickshawDriver = false
        val flags = PackageManager.GET_META_DATA or
                PackageManager.GET_SHARED_LIBRARY_FILES or
                PackageManager.GET_UNINSTALLED_PACKAGES
        val pm = context.packageManager
        val applications = pm.getInstalledApplications(flags)
        for (appInfo in applications) {
            if (!appInfo.packageName.contains("com.gcs.telerickshaw")) {
                telerickshawDriver =
                    appInfo.packageName.contains("com.telerickshaw") || appInfo.packageName.contains(
                        "telerickshaw"
                    )
                if (telerickshawDriver) {
                    break
                }
            }
        }
        return telerickshawDriver
    }


    //	isAppInstalled("com.autoncab.driver");
    open fun isAppInstalled(context: Context, packageName: String?): Boolean {
        val pm = context.packageManager
        var installed = false
        installed = try {
            pm.getPackageInfo(packageName!!, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return installed
    }


    fun olaInstall(context: Context): Boolean {
        // Flags: See below
        var olaDriver = false
        val flags = PackageManager.GET_META_DATA or
                PackageManager.GET_SHARED_LIBRARY_FILES or
                PackageManager.GET_UNINSTALLED_PACKAGES
        val pm = context.packageManager
        val applications = pm.getInstalledApplications(flags)
        for (appInfo in applications) {
            if (!appInfo.packageName.contains("com.olacabs.customer")) {
                olaDriver =
                    appInfo.packageName.contains("com.ola") || appInfo.packageName.contains("olacabs")
                if (olaDriver) {
                    break
                }
            } else if (appInfo.packageName.contains("com.olacabs.kpdriver")) {
                olaDriver = true
            }
        }
        return olaDriver
    }


}

