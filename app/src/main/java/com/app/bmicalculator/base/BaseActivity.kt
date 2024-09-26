package com.app.bmicalculator.base


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.app.bmicalculator.R
import com.app.bmicalculator.model.pojo.auth.authentication.UserData
import com.app.bmicalculator.network.retrofit.Event
import com.app.bmicalculator.utils.Const
import com.app.bmicalculator.utils.InternetCallBack
import com.app.bmicalculator.utils.Prefs
import com.app.bmicalculator.utils.extensions.log
import com.app.bmicalculator.utils.snackBar.ActionClickListener
import com.app.bmicalculator.utils.snackBar.Snackbar
import com.app.bmicalculator.utils.snackBar.SnackbarManager
import com.app.bmicalculator.utils.snackBar.SnackbarType
import com.app.bmicalculator.view.activity.MainActivity
import com.app.bmicalculator.viewModels.base.BaseViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import java.util.Locale


abstract class BaseActivity : AppCompatActivity() {
    var isNetwork = false

    val viewModel: BaseViewModel by lazy {
        ViewModelProvider(this)[BaseViewModel::class.java]
    }

    var internetCallBack: InternetCallBack? = null
    val networkConnectionChangeLiveData = MutableLiveData<Event<Boolean>>()
    var store: Prefs? = null
    private var exit: Boolean = false
    private var networkStatus: String? = null

    val deviceToken: String?
        get() = store!!.getString(Const.DEVICE_TOKEN, uniqueDeviceId)


    val uniqueDeviceId: String
        @SuppressLint("HardwareIds") get() = Settings.Secure.getString(
            contentResolver, Settings.Secure.ANDROID_ID
        )


    private val connectivityManager: ConnectivityManager by lazy {
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        updateStatusBarColor(R.color.white, false)


        strictModeThread()
        transitionSlideInHorizontal()
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
        if (showTitleBar()) supportActionBar?.show()
        else supportActionBar?.hide() //AndroidInjection.inject(this)
        store = Prefs(this)

    }


    fun setOnInternetCallBackListener(internetCallBack: InternetCallBack) {
        this.internetCallBack = internetCallBack
    }


    fun initFCM() {
        /*if (checkPlayServices()) {
        }*/
//        gotoContractorMainActivity()
        gotoMainActivity()
    }

    fun saveOnBoardingComplete(isDone: Boolean = false) {
        store?.save(Const.INTRO_SCREEN, isDone)
    }

    fun getOnBoardingComplete(): Boolean {
        return store?.getBoolean(Const.INTRO_SCREEN, false) ?: false

    }


    /* fun saveUserData(data: LoginDataItem?) {
         viewModel.saveUserData(data)


     }

     fun saveToken(token: String?) {
         viewModel.saveToken(token)

     }

     fun getUserData(): LoginDataItem? {
         return viewModel.getUserData()

     }

     fun saveUserTypeList(userTypeList: ArrayList<LoginDataItem?>?) {
         store!!.saveArrayList(Const.USER_TYPE_LIST, userTypeList)

     }

     fun getUserTypeList(): ArrayList<LoginDataItem?>? {
         return store!!.getArray<LoginDataItem?>(Const.USER_TYPE_LIST)
     }

     fun getAccessToken(): String? {
         return viewModel.getToken()

     }*/


    private fun gotoMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }


    private fun showNoNetworkDialog(status: String?) {
        networkStatus = status
        if (SnackbarManager.currentSnackbar != null) {
            SnackbarManager.currentSnackbar!!.dismiss()
        }
        SnackbarManager.create(
            Snackbar.with(this).type(SnackbarType.SINGLE_LINE).text(status!!).duration(
                Snackbar.SnackbarDuration.LENGTH_INDEFINITE
            ).actionLabel(getString(R.string.retry_caps))
                .actionListener(object : ActionClickListener {
                    override fun onActionClicked(snackbar: Snackbar) {
                        if (!isNetwork) {
                            showNoNetworkDialog(networkStatus)
                        } else {
                            SnackbarManager.currentSnackbar!!.dismiss()
                            internetCallBack?.internetOn(true)
                        }
                    }
                })
        )!!.show()
    }


    private fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(
                    this, resultCode, Const.PLAY_SERVICES_RESOLUTION_REQUEST
                )?.show()
            } else {
                log(getString(R.string.this_device_is_not_supported))
                finish()
            }
            return false
        }
        return true
    }


    abstract fun showTitleBar(): Boolean


    fun hideKeyBoard(input: View?) {

        input?.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(input.windowToken, 0)
        }
    }


    val TAG = "NETWORK_BROADCAST"


    private fun registerNetworkBroadCast(): Boolean {

        val request = NetworkRequest.Builder()
        request.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        request.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        request.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)

        connectivityManager.requestNetwork(
            request.build(),
            object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    Log.i(TAG, "AVAILABLE")
                    isNetwork = true
                    networkConnectionChangeLiveData.postValue(
                        Event(true)
                    )

                }


                override fun onLosing(
                    network: Network,
                    maxMsToLive: Int
                ) {
                    super.onLosing(network, maxMsToLive)
                    Log.i(TAG, "LOSING")
                    isNetwork = false
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    Log.i(TAG, "LOST")
                    isNetwork = false
                    networkConnectionChangeLiveData.postValue(
                        Event(false)
                    )
                }


            })


//         observing network connectivity to display snack bar
        networkConnectionChangeLiveData.observe(this) { it ->

            it?.getContentIfNotHandled()?.let {
                if (!it) {
                    isNetwork = false
                    window.decorView.rootView?.apply {
                        hideKeyBoard(this)
//                        showWarning(this@BaseActivity, getString(R.string.internet_connection))
                        showNoNetworkDialog(getString(R.string.internet_connection))

                    }

                }

            }

        }

        return isNetwork
    }

    fun setSystemBarTheme(isStatusBarFontDark: Boolean) {

        window.decorView.systemUiVisibility = if (isStatusBarFontDark) {
            0
        } else {
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

        }

    }


    fun updateStatusBarColor(
        @ColorRes colorId: Int,
        isStatusBarFontDark: Boolean = true,
        @ColorRes statusBarColor: Int = R.color.transparent
    ) {
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, statusBarColor)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.transparent)
        window.setBackgroundDrawable(ContextCompat.getDrawable(this, colorId))
        setSystemBarTheme(isStatusBarFontDark)
    }


    private var permCallback: PermCallback? = null
    private var reqCode: Int = 0
    fun checkPermissions(
        perms: Array<String>, requestCode: Int, permCallback: PermCallback
    ): Boolean {
        this.permCallback = permCallback
        this.reqCode = requestCode
        val permsArray = ArrayList<String>()
        var hasPerms = true
        for (perm in perms) {
            if (ContextCompat.checkSelfPermission(
                    this, perm
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permsArray.add(perm)
                hasPerms = false
            }
        }
        if (!hasPerms) {
            val permsString = arrayOfNulls<String>(permsArray.size)
            for (i in permsArray.indices) {
                permsString[i] = permsArray[i]
            }
            ActivityCompat.requestPermissions(this@BaseActivity, permsString, Const.PERMISSION_ID)

            return false
        } else return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {

        var permGrantedBool = false
        when (requestCode) {
            Const.PERMISSION_ID -> {
                for (grantResult in grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        log(
                            getString(R.string.not_sufficient_permissions) + getString(R.string.app_name) + getString(
                                R.string.permissions
                            )
                        )
                        /*  showError(
                              this,
                              getString(R.string.not_sufficient_permissions) + getString(R.string.app_name) + getString(
                                  R.string.permissions
                              )
                          )*/

                        permGrantedBool = false
                        break
                    } else {
                        permGrantedBool = true
                    }
                }
                if (permCallback != null) {
                    if (permGrantedBool) {
                        permCallback!!.permGranted(reqCode)
                    } else {
                        permCallback!!.permDenied(reqCode)
                    }
                }
            }
        }



        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    interface PermCallback {
        fun permGranted(resultCode: Int)

        fun permDenied(resultCode: Int)
    }


    fun backAction() {
        when {
            exit -> {
                finishAffinity()
            }

            else -> {
                showToast(getString(R.string.press_one_more_time))
                exit = true
                Handler().postDelayed({ exit = false }, (2 * 1000).toLong())
            }
        }
    }


    fun showToast(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }


    fun showToastOne(msg: String) {
        SnackbarManager.create(
            Snackbar.with(this).duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                .type(SnackbarType.MULTI_LINE).text(msg)
        )!!.show()


    }

    private fun strictModeThread() {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    private fun transitionSlideInHorizontal() {
        this.overridePendingTransition(
            R.anim.slide_in_right, R.anim.slide_out_left
        )
    }


    fun exitFromApp() {
        finish()
        finishAffinity()
    }


    open fun checkLanguage() {

        try {
            val language = store!!.getString(Const.LANGUAGE, Const.ENGLISH)

            val dm = resources.displayMetrics
            val conf = resources.configuration
            conf.setLocale(Locale(language!!.lowercase(Locale.getDefault())))
            resources.updateConfiguration(conf, dm)


        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    fun logOutUser() {
        val isOnBoarding = getOnBoardingComplete()
        store!!.save(Const.USER_TOKEN, "")
        store!!.save(Const.USER_DETAILS, "")
        store!!.removeAll()
        saveOnBoardingComplete(isOnBoarding)
//        gotoLoginSignUpActivity(false)
    }

    @SuppressLint("HardwareIds", "MissingPermission")
    @Throws(SecurityException::class, NullPointerException::class)
    open fun getIMEINumber(): String? {
        try {
            val telephonyManager =
                this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            var imei = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                telephonyManager.imei
            } else {
                telephonyManager.deviceId
            }
            Log.e("imei", "=$imei")
            if (imei == null || imei.isEmpty()) {
                imei = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
            }
            return imei
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return "not_found"
    }


    override fun onStart() {
        store!!.save(Const.FOR_GROUND, true)
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        registerNetworkBroadCast()
        store!!.save(Const.FOR_GROUND, true)
    }

    override fun onPause() {
        super.onPause()
        store!!.save(Const.FOR_GROUND, false)


    }

    override fun onStop() {
        super.onStop()
        store!!.save(Const.FOR_GROUND, false)


    }


    fun saveUserData(data: UserData?) {
        viewModel.saveUserData(data)


    }

    fun saveToken(token: String?) {
        viewModel.saveToken(token)

    }

    fun getUserData(): UserData? {
        return viewModel.getUserData()

    }

    fun getAccessToken(): String? {
        return viewModel.getToken()

    }


}


