package com.app.bmicalculator.utils.extensions


import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions


@SuppressLint("CheckResult")
fun ImageView.loadFromUrl(url: String, defaultImage: Int) {
    if (url.isNotBlank()) {
        val requestOptions = RequestOptions()
        requestOptions.placeholder(defaultImage)
        requestOptions.error(defaultImage)
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(this.context)
            .load(url)
            .apply(requestOptions)
            .into(this)
    } else {
        this.setImageResource(defaultImage)
    }


}

@SuppressLint("CheckResult")
fun ImageView.loadFromUrl(url: Int, defaultImage: Int) {
    val requestOptions = RequestOptions()
    requestOptions.placeholder(defaultImage)
    requestOptions.error(defaultImage)
    requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
    Glide.with(this.context)
        .load(url)
        .apply(requestOptions)
        .into(this)


}

@SuppressLint("CheckResult")
fun ImageView.loadFromUrl(drawable: Int) {
    val requestOptions = RequestOptions()
    requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
    Glide.with(this.context)
        .load(drawable)
        .apply(requestOptions)
        .into(this)


}

@SuppressLint("CheckResult")
fun ImageView.loadFromUrl(url: Uri, defaultImage: Int) {
    val requestOptions = RequestOptions()
    requestOptions.placeholder(defaultImage)
    requestOptions.error(defaultImage)
    requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
    Glide.with(this.context)
        .load(url)
        .apply(requestOptions)
        .into(this)


}

@SuppressLint("CheckResult")
fun ImageView.loadFromUrl(url: Bitmap, defaultImage: Int) {
    val requestOptions = RequestOptions()
    requestOptions.placeholder(defaultImage)
    requestOptions.error(defaultImage)
    requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
    Glide.with(this.context)
        .load(url)
        .apply(requestOptions)
        .into(this)


}





