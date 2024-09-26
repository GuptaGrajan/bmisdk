package com.app.bmicalculator.view.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.app.bmicalculator.R
import com.app.bmicalculator.base.BaseAdapter
import com.app.bmicalculator.base.BaseViewHolder
import com.app.bmicalculator.databinding.AdapterDashBoardBinding
import com.app.bmicalculator.model.pojo.auth.DashBoardModel


class AdapterHome(private val dashList: ArrayList<DashBoardModel>) : BaseAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<AdapterDashBoardBinding>(
            LayoutInflater.from(parent.context), R.layout.adapter_dash_board, parent, false
        )
        return BaseViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val binding = holder.binding as AdapterDashBoardBinding
        val data = dashList[position]

        binding.root.setOnClickListener {
            onItemClick(position)
        }

    }

    override fun getItemCount(): Int {
        return dashList.size
    }
}
