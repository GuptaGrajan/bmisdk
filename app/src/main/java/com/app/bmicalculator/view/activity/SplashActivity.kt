package com.app.bmicalculator.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.app.bmicalculator.R
import com.app.bmicalculator.base.BaseActivity
import com.app.bmicalculator.databinding.ActivitySplashBinding
import com.app.bmicalculator.utils.InternetCallBack
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity(), InternetCallBack {
    private var binding: ActivitySplashBinding? = null


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(binding!!.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        hideKeyBoard(this.currentFocus)
        updateStatusBarColor(R.drawable.splash_screen, false)
        gotoNextScreen()
    }


    override fun showTitleBar(): Boolean {
        return true
    }


    private fun gotoNextScreen() {
        lifecycleScope.launch(Dispatchers.Main) {
            delay(100)
            initFCM()
        }
    }

    override fun internetOn(networkAvailable: Boolean) {
        gotoNextScreen()
    }


}