package com.app.bmicalculator.base

import androidx.databinding.ViewDataBinding


open class BaseViewHolder(var binding: ViewDataBinding?) :
    androidx.recyclerview.widget.RecyclerView.ViewHolder(binding?.root!!)
