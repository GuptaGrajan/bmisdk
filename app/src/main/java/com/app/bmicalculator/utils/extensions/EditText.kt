package com.app.bmicalculator.utils.extensions


import android.annotation.SuppressLint
import android.app.Service
import android.graphics.Typeface
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.TextView.BufferType
import androidx.core.content.ContextCompat
import com.app.bmicalculator.R
import com.app.bmicalculator.utils.Const
import com.app.bmicalculator.utils.Const.Drawable.END
import com.app.bmicalculator.utils.Const.Drawable.START
import com.google.android.material.textfield.TextInputEditText
import java.util.regex.Pattern

fun containsZeroAfterDecimal(value: Double): Boolean {
    val stringValue = value.toString()
    return stringValue.contains(".0")
}


fun View.visibilityView(isShow: Boolean) {
    if (isShow) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun View.visibilityInvisibleView(isShow: Boolean) {
    if (isShow) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.INVISIBLE
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String, TextWatcher) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString(), this)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun EditText.addCurrencyValue(currency: String = "$") {
    this.addTextChangedListener(object : TextWatcher {
        @SuppressLint("SetTextI18n")
        override fun afterTextChanged(s: Editable?) {

            if (s.toString().isNotEmpty() && !s.toString().startsWith(currency)) {
                this@addCurrencyValue.setText(currency + s.toString())
                this@addCurrencyValue.setSelection(this@addCurrencyValue.text.length)
            } else if (s.toString().length == 1) {
                this@addCurrencyValue.setText("")
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}


fun EditText.onTextChanged(onTextChanged: (String, TextWatcher) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged.invoke(s.toString(), this)
        }
    })
}


fun EditText.isBlank(): Boolean {
    return this.text.toString().trim().isEmpty()
}


fun EditText.getLength(): Int {
    return this.text.toString().trim().length
}

fun EditText.checkString(): String {
    return this.text.toString().trim()
}

fun TextView.isBlank(): Boolean {
    return this.text.toString().trim().isEmpty()
}


fun TextView.getLength(): Int {
    return this.text.toString().trim().length
}

fun TextView.checkString(): String {
    return this.text.toString().trim()
}


fun TextInputEditText.setStrokeColor(color: Int) {
    this.setHintTextColor(color)
}

@SuppressLint("ClickableViewAccessibility")
fun EditText.onDrawableClick(drawableType: Int, onClick: () -> Unit) {
    this.setOnTouchListener(View.OnTouchListener { v, event ->

        if (event.action == MotionEvent.ACTION_UP) {
            when (drawableType) {
                END -> {
                    when {
                        event.rawX >= this.right - (this.compoundDrawables.get(END).bounds.width() + this.paddingRight) -> { // your action here
                            onClick()
                            return@OnTouchListener true
                        }
                    }
                }

                START -> {
                    when {
                        event.rawX <= (this.compoundDrawables[START].bounds.width() + this.paddingStart) -> {
                            onClick()

                            return@OnTouchListener true
                        }
                    }
                }
            }
        }
        false
    })
}


fun EditText.isPasswordVisible(drawableStart: Int = 0) {

    if (this.transformationMethod.equals(PasswordTransformationMethod.getInstance())) {
        this.setCompoundDrawablesRelativeWithIntrinsicBounds(
            drawableStart,
            0,
            R.drawable.ic_visibility,
            0
        )
        //Show Password
        this.transformationMethod = HideReturnsTransformationMethod.getInstance()
    } else {
        this.setCompoundDrawablesRelativeWithIntrinsicBounds(
            drawableStart,
            0,
            R.drawable.ic_visibility_off,
            0
        )
        //Hide Password
        this.transformationMethod = PasswordTransformationMethod.getInstance()
    }
}

@SuppressLint("ClickableViewAccessibility")
fun TextView.onDrawableClick(drawableType: Int, onClick: () -> Unit) {
    this.setOnTouchListener(View.OnTouchListener { v, event ->

        if (event.action == MotionEvent.ACTION_DOWN) {
            when (drawableType) {
                END -> {
                    when {
                        event.rawX >= this.right - (this.compoundDrawables.get(END).bounds.width() + this.paddingRight) -> { // your action here
                            onClick()
                            return@OnTouchListener true
                        }
                    }
                }

                START -> {
                    if (event.rawX <= (this.compoundDrawables[START].bounds.width() + this.paddingStart)) {
                        onClick()

                        return@OnTouchListener true
                    }
                }
            }
        }
        false
    })
}


fun TextView.setSpanString(
    spanText: String, start: Int, end: Int = spanText.length,
    showBold: Boolean = false, color: Int = R.color.colorPrimary, onSpanClick: () -> Unit = {}
) {
    val ss = SpannableString(spanText)
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(textView: View) {
            onSpanClick()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            ds.color = ContextCompat.getColor(this@setSpanString.context, color)
        }
    }

    ss.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    if (showBold) {
        ss.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    text = ss
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = ContextCompat.getColor(this@setSpanString.context, R.color.transparent)
}

fun TextView.setSpanString(
    spanText: String,
    start: Int,
    end: Int,
    start2: Int,
    end2: Int = spanText.length,
    showBold: Boolean = false,
    color: Int = R.color.colorPrimary,
    color2: Int = R.color.colorPrimary,
    onSpanClick: (value: Int) -> Unit = {}
) {
    val ss = SpannableString(spanText)

    val termsAndCondition: ClickableSpan = object : ClickableSpan() {
        override fun onClick(textView: View) {
            onSpanClick(Const.SPAN_ONE)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            ds.color = ContextCompat.getColor(this@setSpanString.context, color)
        }
    }

    val privacy: ClickableSpan = object : ClickableSpan() {
        override fun onClick(textView: View) {
            onSpanClick(Const.SPAN_TWO)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            ds.color = ContextCompat.getColor(this@setSpanString.context, color2)
        }
    }

    ss.setSpan(termsAndCondition, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    ss.setSpan(privacy, start2, end2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    if (showBold) {
        ss.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(StyleSpan(Typeface.BOLD), start2, end2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    this.setText(ss, BufferType.SPANNABLE)
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = ContextCompat.getColor(this@setSpanString.context, R.color.transparent)
}


fun TextView.setSpanString(
    spanText: String,
    start: Int,
    end: Int,
    start2: Int,
    end2: Int,
    start3: Int,
    end3: Int,
    showBold: Boolean = false,
    color: Int = R.color.colorPrimary,
    color2: Int = R.color.colorPrimary,
    color3: Int = R.color.colorPrimary,
    onSpanClick: (value: Int) -> Unit = {}
) {
    val ss = SpannableString(spanText)

    val termsAndCondition: ClickableSpan = object : ClickableSpan() {
        override fun onClick(textView: View) {
            onSpanClick(Const.SPAN_ONE)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            ds.color = ContextCompat.getColor(this@setSpanString.context, color)
        }
    }

    val privacy: ClickableSpan = object : ClickableSpan() {
        override fun onClick(textView: View) {
            onSpanClick(Const.SPAN_TWO)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            ds.color = ContextCompat.getColor(this@setSpanString.context, color2)
        }
    }

    val thirdString: ClickableSpan = object : ClickableSpan() {
        override fun onClick(textView: View) {
            onSpanClick(Const.SPAN_THREE)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            ds.color = ContextCompat.getColor(this@setSpanString.context, color3)
        }
    }

    ss.setSpan(termsAndCondition, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    ss.setSpan(privacy, start2, end2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    ss.setSpan(thirdString, start3, end3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    ss.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    if (showBold) {
        ss.setSpan(StyleSpan(Typeface.BOLD), start2, end2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(StyleSpan(Typeface.BOLD), start3, end3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    this.setText(ss, BufferType.SPANNABLE)
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = ContextCompat.getColor(this@setSpanString.context, R.color.transparent)
}


fun String.isValidEmail(): Boolean =
    this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()


fun EditText.isValidEmail(): Boolean =
    this.checkString().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this.checkString()).matches()


fun EditText.isValidPassword(): Boolean {
    return Pattern
        .compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!&^%$#@()=*/.+_-])(?=\\S+$).{8,}$")
        .matcher(this.checkString()).matches()
}

fun String.isValidPassword(): Boolean {
    return Pattern
        .compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!&^%$#@()=*/.+_-])(?=\\S+$).{8,}$")
        .matcher(this.checkString()).matches()
}

fun EditText.isHashTag(): Boolean {
    return Pattern.compile("[#]+[A-Za-z0-9-_]+\\b")
        .matcher(this.checkString()).matches()
}

fun String.isHashTag(): Boolean {
    return Pattern.compile("[#]+[A-Za-z0-9-_]+\\b")
        .matcher(this.checkString()).matches()
}

fun EditText.isValidName(): Boolean {
    return Pattern
        .compile("^(?=.*[A-Za-z])(?=\\S+\$).{3,}$")
        .matcher(checkString()).matches()
}


fun EditText.checkIbanNo(): Boolean {
    return Pattern.compile("^[SA]{2}\\d{22}").matcher(this.checkString()).matches()
}

fun TextView.checkIbanNo(): Boolean {
    return Pattern.compile("^[SA]{2}\\d{22}").matcher(this.checkString()).matches()
}

fun String.checkIbanNo(): Boolean {
    return Pattern.compile("^[SA]{2}\\d{22}").matcher(this).matches()
}

fun String.isValidName(): Boolean {
    return Pattern
        .compile("^(?=.*[A-Za-z])(?=\\S+\$).{3,}$")
        .matcher(checkString()).matches()
}

fun String.isBlank(): Boolean {
    return this.trim().isEmpty()
}


fun String.getLength(): Int {
    return this.trim().length
}

fun String.checkString(): String {
    return this.trim()
}


fun EditText.setFocus() {

    isFocusable = true
    isFocusableInTouchMode = true
    requestFocus()
}

fun EditText.showSoftKeyboard() {
    val imm = context.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, 0)
}


fun EditText.removeSpecialChar(s: String): String {
    return this.text.toString().trim().replace(s, "")
}

fun View.changeColor(isSelected: Boolean) {
    if (isSelected) {
        this.background = ContextCompat.getDrawable(this.context, R.drawable.theme_button_bg)
    } else {
        this.background = ContextCompat.getDrawable(this.context, R.drawable.disable_button_bg)
    }
}


fun EditText.preventMultipleSpaces() {

    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Not needed for this example
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Not needed for this example
        }

        override fun afterTextChanged(s: Editable?) {
            s?.let {
                val input = it.toString()
                val result = input.replace(
                    "\\s+".toRegex(),
                    " "
                ) // Replace multiple spaces with a single space
                if (input != result) {
                    this@preventMultipleSpaces.setText(result)
                    this@preventMultipleSpaces.setSelection(result.length) // Set cursor position to the end
                }
            }
        }
    })

//    this.preventLeadingTrailingSpaces()
}


fun EditText.preventSpacesInPassword() {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Not needed for this example
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Not needed for this example
        }

        override fun afterTextChanged(s: Editable?) {
            s?.let {
                val input = it.toString()
                if (input.contains(" ")) {
                    // If space is detected, remove it
                    val result = input.replace(" ", "")
                    this@preventSpacesInPassword.setText(result)
                    this@preventSpacesInPassword.setSelection(result.length) // Set cursor position to the end
                }
            }
        }
    })
}


fun EditText.preventLeadingTrailingSpaces() {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Not needed for this example
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Not needed for this example
        }

        override fun afterTextChanged(s: Editable?) {
            s?.let {
                val input = it.toString()
                if (input.startsWith(" ") || input.endsWith(" ")) {
                    // Trim leading and trailing spaces
                    val result = input.trim()
                    this@preventLeadingTrailingSpaces.setText(result)
                    this@preventLeadingTrailingSpaces.setSelection(result.length) // Set cursor position to the end
                }
            }
        }
    })
}



