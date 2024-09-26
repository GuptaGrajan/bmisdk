package com.app.bmicalculator.utils.extensions

import android.util.Log


fun log(string: String?) {
    /* if (BuildConfig.DEBUG) {

     }*/
    Log.e("BaseActivity", string!!)

}