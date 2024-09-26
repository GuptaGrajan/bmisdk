package com.app.bmicalculator.utils

import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException

class Api3Params {
    private var requestParentMap: HashMap<String, Any>? = null
    private var requestChildMap: HashMap<String, String>? = null

    private var requestMultipartChildMap: HashMap<String, RequestBody>? = null
    private var requestMultipartUriChildMap: HashMap<String, Uri>? = null

    private val childObject: JSONObject
        get() = JSONObject(requestChildMap as Map<*, *>)

    init {

        requestParentMap = HashMap()
        requestChildMap = HashMap()
        requestMultipartChildMap = HashMap()
        requestMultipartUriChildMap = HashMap()
    }

    fun put(key: String, value: String?) {
        requestChildMap?.put(key, value.toString())
    }

    fun put(key: String, value: Float) {
        requestChildMap?.put(key, value.toString())
    }

    fun put(key: String, value: Double) {
        requestChildMap?.put(key, value.toString())
    }

    fun put(key: String, value: Int) {
        requestChildMap?.put(key, value.toString())
    }

    fun put(key: String, value: JSONArray) {
        requestChildMap?.put(key, value.toString())
    }

    fun put(key: String, value: Uri) {
        requestMultipartUriChildMap?.put(key, value)
    }

    fun putMultipart(key: String, value: String) {
        requestMultipartChildMap?.put(key, toRequestBody(value))
    }

    fun getServerObject(parent: String): JSONObject {
        requestParentMap?.put(parent, childObject)
        return JSONObject(requestParentMap as Map<*, *>)
    }

    fun getServerHashMap(): HashMap<String, String>? {
        return requestChildMap
    }

    fun getServerMultipartUriHashMap(): HashMap<String, Uri>? {
        return requestMultipartUriChildMap
    }

    fun getServerMultipartHashMap(): HashMap<String, RequestBody>? {
        return requestMultipartChildMap
    }

    // This method  converts String to RequestBody
    private fun toRequestBody(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    // This method  converts String to RequestBody
    fun toRequestImage(key: String, file: File?): MultipartBody.Part {
        if (file != null && file.exists()) {
            val fileReqBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            return MultipartBody.Part.createFormData(key, file.name, fileReqBody)
        } else {
            throw FileNotFoundException()
        }
    }
}