package com.app.bmicalculator.view.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.app.bmicalculator.R
import com.app.bmicalculator.base.BaseActivity
import com.app.bmicalculator.databinding.ActivityMainBinding
import com.app.bmicalculator.utils.extensions.replaceFragmentWithoutStack
import com.app.bmicalculator.view.fragmets.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private var binding: ActivityMainBinding? = null
    override fun showTitleBar() = true


    private val onBackPressedCallBack = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val fragment = supportFragmentManager.findFragmentById(R.id.frameContainerFL)

            when {
                fragment is HomeFragment -> {
                    backAction()
                }


                supportFragmentManager.backStackEntryCount > 0 -> {
                    supportFragmentManager.popBackStack()
                }

                else -> {

                    gotoHomeScreen()
                }
            }
        }

    }

    private fun gotoHomeScreen() {
        replaceFragmentWithoutStack(HomeFragment())

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(binding!!.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        hideKeyBoard(this.currentFocus)
        updateStatusBarColor(R.color.colorPrimary, false)
        initUI()
    }

    private fun initUI() {
        onBackPressedDispatcher.addCallback(this, onBackPressedCallBack)
        gotoHomeScreen()

    }
}