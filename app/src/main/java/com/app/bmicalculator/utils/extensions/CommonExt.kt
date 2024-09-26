package com.app.bmicalculator.utils.extensions


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Typeface
import android.location.Location
import android.net.ConnectivityManager
import android.os.BatteryManager
import android.text.InputFilter
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.webkit.CookieManager
import android.webkit.WebView
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.bmicalculator.App
import com.app.bmicalculator.R
import com.app.bmicalculator.utils.Const
import com.app.bmicalculator.utils.Prefs
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.sin


fun RecyclerView.getRecyclerViewHeight(listSize: Int) {
    val layoutParams = this.layoutParams
    layoutParams.height = if (listSize < 5) WindowManager.LayoutParams.WRAP_CONTENT else 300
    this.layoutParams = layoutParams
}

fun String.isStrongPassword(): Boolean {
    // Regex to check for at least one uppercase letter
    val upperCasePattern = "(.*[A-Z].*)"
    // Regex to check for at least one lowercase letter
    val lowerCasePattern = "(.*[a-z].*)"
    // Regex to check for at least one digit
    val digitPattern = "(.*\\d.*)"
    // Regex to check for at least one special character
    val specialCharacterPattern = "(.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*)"

    // Check the length of the password
    if (this.length < 8) {
        return false
    }

    // Check for the presence of each pattern
    if (!this.matches(Regex(upperCasePattern))) {
        return false
    }
    if (!this.matches(Regex(lowerCasePattern))) {
        return false
    }
    if (!this.matches(Regex(digitPattern))) {
        return false
    }
    if (!this.matches(Regex(specialCharacterPattern))) {
        return false
    }

    return true
}


fun View.setBounceAnim() {
    this.startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.bounce_anim))
}

fun View.setSlideInAnim() {
    this.startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.slide_in_right))
}

fun View.setWrongShakeAnim() {
    this.startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.shake_animation))
}

fun View.hideKeyboard(isHide: Boolean = true) {
    if (isHide) {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}


fun View.preventDoubleClick() {
    this.isClickable = false
    this.postDelayed({ this.isClickable = true }, 1000)
}


fun removeAllCookies() {
    val cookieManager = CookieManager.getInstance()
    cookieManager.removeAllCookies(null)
    cookieManager.removeSessionCookies(null)
}

fun TextView.readMoreReadLess(maxLineCount: Int = 150, isExpanded: Boolean = false) {

    /* val spannableString = SpannableString(this.checkString())
     if (this.getLength() > maxLineCount) {
         if (!isExpanded) {
             spannableString.setSpan(
                 object : ClickableSpan() {
                     override fun onClick(widget: View) {
                         isExpanded = true
                         readMoreReadLess(maxLineCount)
                     }
                 },
                 this.length() - "Read More".length,
                 this.length(),
                 Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
             )
         }

         else {
             spannableString.setSpan(
                 object : ClickableSpan() {
                     override fun onClick(widget: View) {
                         isExpanded = false
                         readMoreReadLess(maxLineCount)
                     }
                 },
                 this.length() - "Read Less".length,
                 this.length(),
                 Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
             )
         }
     }

     this.text = spannableString
     this.movementMethod = LinkMovementMethod.getInstance()*/


    if (!isExpanded) {
        if (this.getLength() > maxLineCount) {
            val displayText: String =
                this.checkString().substring(0, maxLineCount) + " ...Read More"
            val spannableString = SpannableString(displayText)
            val clickableSpan: ClickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    readMoreReadLess(isExpanded = true)
                }
            }
            spannableString.setSpan(
                clickableSpan,
                displayText.length - 10,
                displayText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            this.setText(spannableString)
            this.setMovementMethod(LinkMovementMethod.getInstance())
        }
    } else {
        val spannableString = SpannableString(this.checkString() + " Read Less")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                readMoreReadLess(isExpanded = false)
            }
        }
        spannableString.setSpan(
            clickableSpan,
            this.getLength(),
            this.getLength() + 9,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        this.setText(spannableString)
        this.setMovementMethod(LinkMovementMethod.getInstance())
    }
}


@SuppressLint("SetJavaScriptEnabled")
fun WebView.webViewCommonSetting() {
    this.settings.javaScriptEnabled = true
    this.settings.domStorageEnabled = true
    this.settings.useWideViewPort = true
    this.setPadding(0, 0, 0, 0)
    this.settings.setSupportZoom(true)
    this.settings.builtInZoomControls = true
    this.settings.builtInZoomControls = true
}


val decimalFormatEXT = DecimalFormat("#.##", DecimalFormatSymbols(Locale.ENGLISH))
val decimalFormatOneDecimalEXT = DecimalFormat("#.#", DecimalFormatSymbols(Locale.ENGLISH))
val decimalFormatNoDecimalEXT = DecimalFormat("#", DecimalFormatSymbols(Locale.ENGLISH))


fun Context.getActualBatteryPer(): String? {
    return try {
        val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = this.registerReceiver(null, ifilter)
        val level = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val batteryPct = level / scale.toFloat() * 100

        // Are we charging / charged?
        val status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL
        batteryPct.toString()
    } catch (e: Exception) {
        e.printStackTrace()
        "0"
    }
}


fun Context.isBatteryChargingNew(): Int {
    return try {
        val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = this.registerReceiver(null, ifilter)
        val level = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val batteryPct = level / scale.toFloat() * 100
        // Are we charging / charged?
        val status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL
        if (isCharging) {
            1
        } else {
            0
        }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        0
    }
}


fun Context.isMobileDataEnable(): Boolean {
    var mobileDataEnabled = false // Assume disabled
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    try {
        val cmClass = Class.forName(cm.javaClass.name)
        val method = cmClass.getDeclaredMethod("getMobileDataEnabled")
        method.isAccessible = true // Make the method callable
        // get the setting for "mobile data"
        mobileDataEnabled = method.invoke(cm) as Boolean
    } catch (e: java.lang.Exception) {
        // Some problem accessible private API and do whatever error handling you want here
    }
    return mobileDataEnabled
}


fun getDistanceKMBWTwoLatLong(
    latitude1: Double,
    longitude1: Double,
    latitude2: Double,
    longitude2: Double
): Double {

    val longDiff = longitude1 - longitude2
    var distance = sin(deg2red(latitude1)) *
            sin(deg2red(latitude2)) +
            cos(deg2red(latitude1)) *
            cos(deg2red(latitude2)) *
            cos(deg2red(longDiff))

    distance = acos(distance)
    distance = red2deg(distance)

    distance *= 60 * 1.1515
    distance *= 1.609344

    return distance
}

fun red2deg(distance: Double): Double {
    return (distance * 180.0 / Math.PI)
}

fun deg2red(lat: Double): Double {
    return (lat * Math.PI / 180.0)

}


fun getDistanceBWTwoLatLong(start: Location, end: Location?): Double? {
    try {
        return end?.let { start.distanceTo(it).toDouble() }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return 0.0
}


fun addTextColor(text: String, color: Int): SpannableStringBuilder {
    val ssb = SpannableStringBuilder(text.lowercase(Locale.getDefault()))
    ssb.setSpan(ForegroundColorSpan(color), 0, ssb.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    ssb.setSpan(RelativeSizeSpan(0.7f), 0, ssb.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return ssb
}


fun Int.convertToArabic(): String {

    val local = Prefs(App.appContext!!).getString(Const.LANGUAGE, Const.ENGLISH)

    return if (local == "ar") {
        (this.toString() + "")
            .replace("1".toRegex(), "١").replace("2".toRegex(), "٢")
            .replace("3".toRegex(), "٣").replace("4".toRegex(), "٤")
            .replace("5".toRegex(), "٥").replace("6".toRegex(), "٦")
            .replace("7".toRegex(), "٧").replace("8".toRegex(), "٨")
            .replace("9".toRegex(), "٩").replace("0".toRegex(), "٠")

    } else {
        this.toString()
    }


}

fun Double.convertToArabic(): String {

    val local = Prefs(App.appContext!!).getString(Const.LANGUAGE, Const.ENGLISH)

    return if (local == "ar") {
        (this.toString() + "")
            .replace("1".toRegex(), "١").replace("2".toRegex(), "٢")
            .replace("3".toRegex(), "٣").replace("4".toRegex(), "٤")
            .replace("5".toRegex(), "٥").replace("6".toRegex(), "٦")
            .replace("7".toRegex(), "٧").replace("8".toRegex(), "٨")
            .replace("9".toRegex(), "٩").replace("0".toRegex(), "٠")

    } else {
        this.toString()
    }


}


fun String.convertToArabic(): String {

    val local = Prefs(App.appContext!!).getString(Const.LANGUAGE, Const.ENGLISH)

    return if (local == "ar") {
        (this)
            .replace("1".toRegex(), "١").replace("2".toRegex(), "٢")
            .replace("3".toRegex(), "٣").replace("4".toRegex(), "٤")
            .replace("5".toRegex(), "٥").replace("6".toRegex(), "٦")
            .replace("7".toRegex(), "٧").replace("8".toRegex(), "٨")
            .replace("9".toRegex(), "٩").replace("0".toRegex(), "٠")

    } else {
        this
    }


}

fun String.convertArabicToNumeric(): String {

    val local = Prefs(App.appContext!!).getString(Const.LANGUAGE, Const.ENGLISH)

    return if (local == "ar") {
        (this)
            .replace("١".toRegex(), "1").replace("٢".toRegex(), "2")
            .replace("٣".toRegex(), "3").replace("٤".toRegex(), "4")
            .replace("٥".toRegex(), "5").replace("٦".toRegex(), "6")
            .replace("٧".toRegex(), "7").replace("٨".toRegex(), "8")
            .replace("٩".toRegex(), "9").replace("٠".toRegex(), "0")

    } else {
        this
    }


}


fun Int.setPagination() {

}

fun changeDateFormat(
    dateString: String?,
    sourceDateFormat: String?,
    targetDateFormat: String?
): String {
    if (dateString == null || dateString.isEmpty()) {
        return ""
    }
    val inputDateFromat = SimpleDateFormat(sourceDateFormat, Locale.US)
    var date: Date? = Date()
    try {
        date = inputDateFromat.parse(dateString)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    val outputDateFormat = SimpleDateFormat(targetDateFormat, Locale.US)
    return date?.let { outputDateFormat.format(it) }.toString()
}


fun changeDateFormatFromDate(sourceDate: Date?, targetDateFormat: String?): String {
    if (sourceDate == null || targetDateFormat == null || targetDateFormat.isEmpty()) {
        return ""
    }
    val outputDateFormat = SimpleDateFormat(targetDateFormat, Locale.US)
    return outputDateFormat.format(sourceDate)
}


fun Long.getFormatedNumber(): String {
    if (this < 1000) return "" + this

    val exp = (ln(this.toDouble()) / ln(1000.0)).toInt()


    return if (containsZeroAfterDecimal(this / 1000.0.pow(exp.toDouble()))) {
        String.format("%d%c", (this / 1000.0.pow(exp.toDouble())).toInt(), "kMGTPE"[exp - 1])
    } else {
        String.format("%.1f%c", this / 1000.0.pow(exp.toDouble()), "kMGTPE"[exp - 1])

    }


}

fun EditText.disableEmojiInTitle() {
    val emojiFilter =
        InputFilter { source, start, end, dest, dstart, dend ->
            for (index in start until end - 1) {
                val type = Character.getType(source[index])
                if (type == Character.SURROGATE.toInt()) {
                    return@InputFilter ""
                }
            }
            null
        }
    this.filters = arrayOf(emojiFilter)
}


fun String.isEmail(): Boolean {
    val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
    val p: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val m: Matcher = p.matcher(this)
    return m.matches()
}

fun String.isPhone(): Boolean {
    return if (!TextUtils.isEmpty(this)) {
        TextUtils.isDigitsOnly(this)
    } else {
        false
    }
}


fun TextView.setTextWithDifferentSizes(amount: String, monthText: String) {
    // Create the complete text
    val fullText = amount + monthText

    // Create a SpannableString to style different parts of the text
    val spannableString = SpannableString(fullText)

    // Set the size of the amount text
    val amountLength = amount.length

    spannableString.setSpan(RelativeSizeSpan(1.5f), 0, amountLength, 0)
    spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, amountLength, 0)

    // Set the size of the "Month" text
    spannableString.setSpan(RelativeSizeSpan(0.6f), amountLength, fullText.length, 0)

    // Apply the styled text to the TextView
    this.text = spannableString
}


fun getMimeTypeFromStringUrl(url: String): String? {
    return when {

        getFileExtensionFromUrl(url).equals(".doc") || getFileExtensionFromUrl(url).equals(".docx") -> {
            // Word document
            "application/msword"

        }

        getFileExtensionFromUrl(url).equals(".pdf") -> {
            // PDF file
            "application/pdf"
        }

        getFileExtensionFromUrl(url).equals(".xls") || getFileExtensionFromUrl(url).equals(".xlsx") -> {
            // Excel file

            "application/vnd.ms-excel"

        }


        getFileExtensionFromUrl(url).equals(".jpg") || getFileExtensionFromUrl(url).equals(".jpeg") || getFileExtensionFromUrl(
            url
        ).equals(".png") -> {
            // JPG file
            "image/jpeg"
        }

        getFileExtensionFromUrl(url).equals(".mp4") -> {
            // Video files
            "video/mp4"
        }

        getFileExtensionFromUrl(url).equals(".wav") -> {
            // Video files
            "video/wav"
        }

        getFileExtensionFromUrl(url).equals(".mov") -> {
            // Video files
            "video/mov"
        }

        else -> {
            //if you want you can also define the intent type for any other file

            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            getFileExtensionFromUrl(url)
        }
    }

}


fun getFileExtensionFromUrl(url: String?): String? {
    var extension: String? = null
    if (url != null) {
        val lastDotIndex = url.lastIndexOf('.')
        if (lastDotIndex >= 0) {
            val substring = url.substring(lastDotIndex + 1)
            extension = substring
        }
    }
    return ".${extension}"
}

