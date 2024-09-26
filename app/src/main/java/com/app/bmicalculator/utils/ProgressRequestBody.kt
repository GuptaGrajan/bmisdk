package com.app.bmicalculator.utils

import android.os.Handler
import android.os.Looper
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class ProgressRequestBody(
    private val file: File,
    private val contentType: String,
    private val listener: UploadProgressListener
) : RequestBody() {
    private var isCancelled = false

    private val path: String? = null
    override fun contentType(): MediaType? {
        return "$contentType/*".toMediaTypeOrNull()
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return file.length()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val fileLength = file.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val `in` = FileInputStream(file)
        var uploaded: Long = 0
        try {
            var read = 0
            val handler = Handler(Looper.getMainLooper())
            while (!isCancelled && `in`.read(buffer).also { read = it } != -1) {
                // update progress on UI thread
                handler.post(ProgressUpdater(uploaded, fileLength))
                uploaded += read.toLong()
                sink.write(buffer, 0, read)
            }

        } finally {
            `in`.close()
        }
    }

    interface UploadProgressListener {
        fun onProgressUpdate(percentage: Int)
        fun onCancelled()
    }

    fun cancel() {
        isCancelled = true
        listener.onCancelled()
    }

    private inner class ProgressUpdater(private val mUploaded: Long, private val mTotal: Long) :
        Runnable {
        override fun run() {
            listener.onProgressUpdate((100 * mUploaded / mTotal).toInt())
        }
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 2048
    }
}