package com.app.bmicalculator.view.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.app.bmicalculator.R
import com.app.bmicalculator.base.BaseAdapter
import com.app.bmicalculator.base.BaseViewHolder
import com.app.bmicalculator.databinding.AdapterBmiResultBinding
import com.app.bmicalculator.model.pojo.auth.DashBoardModel


class AdapterBmiResult(private val dashList: ArrayList<DashBoardModel>) : BaseAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<AdapterBmiResultBinding>(
            LayoutInflater.from(parent.context), R.layout.adapter_bmi_result, parent, false
        )
        return BaseViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val binding = holder.binding as AdapterBmiResultBinding
        val data = dashList[position]
        /* val wrappedDrawable = DrawableCompat.wrap(binding.mainCL.background!!).mutate()
         if (data.isSelected) {
             DrawableCompat.setTint(
                 wrappedDrawable, ContextCompat.getColor(holder.itemView.context, data.color)
             )
             binding.mainCL.background = wrappedDrawable
         } else {
             DrawableCompat.setTint(
                 wrappedDrawable,
                 ContextCompat.getColor(holder.itemView.context, R.color.tint)
             )
             binding.mainCL.background = wrappedDrawable
         }*/



        if (data.isSelected) {
            binding.titleTV.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    data.color
                )
            )
            binding.valueTV.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    data.color
                )
            )
        } else {
            binding.titleTV.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.textColorDark
                )
            )
            binding.valueTV.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.textColorDark
                )
            )
        }
        binding.titleTV.text = data.title
        binding.valueTV.text = data.value
        binding.titleTV.setCompoundDrawablesRelativeWithIntrinsicBounds(
            data.drawable, null, null, null
        )


    }

    override fun getItemCount(): Int {
        return dashList.size
    }
}
