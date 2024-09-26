package com.app.bmicalculator.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class SocketClient(private val messageListener: MessageListener?) {
    private var socket: Socket? = null
    private var output: PrintWriter? = null
    private var input: BufferedReader? = null

    fun connect(serverIp: String?, serverPort: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                socket = Socket(serverIp, serverPort)
                output = PrintWriter(socket!!.getOutputStream(), true)
                input = BufferedReader(InputStreamReader(socket!!.getInputStream()))
                Log.d(TAG, "Connected to server")

                startReceivingMessages()

            } catch (e: IOException) {
                Log.e(TAG, "Error connecting to server: ${e.message}")
            }
        }
    }

    private fun startReceivingMessages() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                var message: String?
                while (input!!.readLine().also { message = it } != null) {
                    messageListener?.onMessageReceived(message)
                }
            } catch (e: IOException) {
                Log.e(TAG, "Error receiving message: ${e.message}")
            }
        }
    }

    fun disconnect() {
        try {
            socket?.close()
        } catch (e: IOException) {
            Log.e(TAG, "Error closing socket: ${e.message}")
        }
    }

    fun sendMessage(message: String?) {
        output?.println(message)
    }

    interface MessageListener {
        fun onMessageReceived(message: String?)
    }

    companion object {
        private const val TAG = "SocketClient"
    }
}

