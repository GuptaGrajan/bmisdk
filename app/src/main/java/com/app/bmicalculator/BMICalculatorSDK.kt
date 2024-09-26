package com.app.bmicalculator

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.app.bmicalculator.view.activity.MainActivity

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BMICalculatorSDK private constructor(private val context: Context) : AppCompatActivity() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: BMICalculatorSDK? = null
        fun getInstance(context: Context): BMICalculatorSDK {
            return instance ?: synchronized(this) {
                instance ?: BMICalculatorSDK(context).also { instance = it }
            }
        }
    }

    fun launchSdk() {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
        finish()
    }



}


