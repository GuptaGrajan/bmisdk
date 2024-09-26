package com.app.bmicalculator.view.fragmets

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.app.bmicalculator.R
import com.app.bmicalculator.base.BaseAdapter
import com.app.bmicalculator.base.BaseFragment
import com.app.bmicalculator.databinding.FragmentHomeBinding
import com.app.bmicalculator.model.pojo.auth.DashBoardModel
import com.app.bmicalculator.utils.HandleClick
import com.app.bmicalculator.utils.extensions.replaceFragment
import com.app.bmicalculator.view.adapter.AdapterHome

class HomeFragment : BaseFragment<FragmentHomeBinding>(), HandleClick,
    BaseAdapter.OnItemClickListener {

    override fun getLayoutRes(): Int = R.layout.fragment_home
    private var dashList: ArrayList<DashBoardModel> = ArrayList()
    private var adapterHome: AdapterHome? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()

    }

    private fun initUI() {
        initDashBoardList()
    }

    override fun onClickHandle(view: View) {
        when (view.id) {

        }
    }

    @SuppressLint("Recycle")
    fun initDashBoardList() {
        dashList.clear()
        val names = resources.getStringArray(R.array.homeTitle)
        for (i in names.indices) {
            dashList.add(DashBoardModel(id = i + 1, title = names[i]))
        }
        setAdapter()
    }

    private fun setAdapter() {
        adapterHome = AdapterHome(dashList)
        binding.homeRV.adapter = adapterHome
        adapterHome?.setOnItemClickListener(this)

    }

    override fun onItemClick(vararg itemData: Any) {
        if (itemData.isNotEmpty()) {
            val position = itemData[0] as Int
            val data = dashList[position]

            baseActivity.replaceFragment(CalculateBmiFragment())

        }

    }


}