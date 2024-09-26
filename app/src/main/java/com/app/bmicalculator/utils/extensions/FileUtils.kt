package com.app.bmicalculator.utils.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


@Throws(IOException::class)
fun uriToFile(context: Context, uri: Uri?): File {
    val contentResolver = context.contentResolver
    val mimeType = contentResolver.getType(uri!!)

    // Get the file extension from the mime type
    val fileExtension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)

    // Create a temporary file with the proper extension
    val file: File = File.createTempFile("temp", ".$fileExtension")
    contentResolver.openInputStream(uri).use { inputStream ->
        FileOutputStream(file).use { outputStream ->
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream!!.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
        }
    }
    return file
}


fun Context.getFile(uri: Uri): File? {
    try {
        val destination = File(this.cacheDir, File(uri.path!!).name)

        val parcelFileDescriptor = this.contentResolver.openFileDescriptor(uri, "r")
        val fileDescriptor = parcelFileDescriptor?.fileDescriptor ?: return null

        val src = FileInputStream(fileDescriptor).channel
        val dst = FileOutputStream(destination).channel
        dst.transferFrom(src, 0, src.size())
        src.close()
        dst.close()

        return destination
    } catch (ex: IOException) {
        ex.printStackTrace()
    }
    return null
}

@SuppressLint("Recycle")
fun Context.getTempFile(uri: Uri): File? {
    try {
        val destination = File(this.cacheDir, File(uri.path!!).name)

        val parcelFileDescriptor = this.contentResolver.openFileDescriptor(uri, "r")
        val fileDescriptor = parcelFileDescriptor?.fileDescriptor ?: return null

        val src = FileInputStream(fileDescriptor).channel
        val dst = FileOutputStream(destination).channel
        dst.transferFrom(src, 0, src.size())
        src.close()
        dst.close()

        return destination
    } catch (ex: IOException) {
        ex.printStackTrace()
    }
    return null
}

fun Context.getFileSizeFromUri(uri: Uri): Long {
    val inputStream = this.contentResolver.openInputStream(uri)
    inputStream?.use {
        return it.available().toLong()
    }
    return 0L
}





