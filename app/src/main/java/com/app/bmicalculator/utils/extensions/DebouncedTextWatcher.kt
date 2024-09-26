package com.app.bmicalculator.utils.extensions

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher

open class DebouncedTextWatcher(private val listener: OnTextChangedListener) : TextWatcher {
    private val handler = Handler(Looper.getMainLooper())
    private val debounceRunnable: Runnable = Runnable { listener.onTextChanged() }

    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        handler.removeCallbacks(debounceRunnable)
    }

    override fun afterTextChanged(editable: Editable) {
        handler.postDelayed(debounceRunnable, DEBOUNCE_DELAY_MS)
    }

    interface OnTextChangedListener {
        fun onTextChanged()
    }

    companion object {
        private const val DEBOUNCE_DELAY_MS: Long = 400 // Adjust as needed
    }
}
