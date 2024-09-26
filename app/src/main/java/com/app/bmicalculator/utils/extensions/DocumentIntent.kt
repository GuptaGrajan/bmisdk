package com.app.bmicalculator.utils.extensions

import android.content.Intent
import android.hardware.camera2.CameraCharacteristics
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.app.bmicalculator.utils.Const


fun getDocumentIntent(
    mimeTypes: Array<String> = arrayOf(
        "application/pdf",
        "application/msword",
        "application/vnd.ms-excel",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"

    ), isMultiple: Boolean = false, type: String = "*/*"
): Intent {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
//    intent.type = ""
    intent.type = type

    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
    if (isMultiple) {
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

    }
    return intent

}

fun getMimeType(url: String?): String? {
    var type: String? = null
    val extension = MimeTypeMap.getFileExtensionFromUrl(url)
    if (extension != null) {
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }
    return type
}


fun getCameraIntent(uri: Uri, tryFrontCamera: Boolean): Intent {

    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
    if (tryFrontCamera) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                intent.putExtra(
                    Const.CAMERA_FACING_EXTRA, CameraCharacteristics.LENS_FACING_FRONT
                ) // Tested on API 27 Android version 8.0(Nexus 6P)
                intent.putExtra(
                    "android.intent.extra.USE_FRONT_CAMERA", true
                ) // tested on android 11
                intent.putExtra(
                    "android.intent.extras.CAMERA_FACING", CameraCharacteristics.LENS_FACING_FRONT
                ) // tested on android 11
                intent.putExtra(
                    "android.intent.extras.CAMERA_FACING",
                    android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT
                ) // tested on android 11
                intent.putExtra(
                    "com.google.assistant.extra.USE_FRONT_CAMERA", true
                )
                intent.putExtra(
                    "android.intent.extras.LENS_FACING_FRONT", 1
                )
                // Samsung
                intent.putExtra("camerafacing", "front")
                intent.putExtra("previous_mode", "front")
                // Huawei
                intent.putExtra("default_camera", "1")
                intent.putExtra("default_mode", "com.huawei.camera2.mode.photo.PhotoMode")
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 && Build.VERSION.SDK_INT < Build.VERSION_CODES.O -> {
                intent.putExtra(
                    Const.CAMERA_FACING_EXTRA, CameraCharacteristics.LENS_FACING_FRONT
                ) // Tested on API 24 Android version 7.0(Samsung S6)
            }

            Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1 -> {
                intent.putExtra(
                    Const.CAMERA_FACING_EXTRA, 1
                )
            }
            // Tested API 21 Android version 5.0.1(Samsung S4)
        }
    }



    return intent
}

fun getVideoIntent(): Intent {
    val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
    return intent


}