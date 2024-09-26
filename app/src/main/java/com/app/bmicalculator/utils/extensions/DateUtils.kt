package com.app.bmicalculator.utils.extensions


import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import kotlin.math.abs

fun getCurrentYear() = Calendar.getInstance().get(Calendar.YEAR)


fun calculateMonthsDifference(startDate: Date, endDate: Date): Int {
    val startCalendar = Calendar.getInstance()
    startCalendar.time = startDate

    val endCalendar = Calendar.getInstance()
    endCalendar.time = endDate

    val yearsDiff = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR)
    val monthsDiff = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH)

    return yearsDiff * 12 + monthsDiff
}


fun String?.changeDateFormat(
    sourceDateFormat: String = "yyyy-MM-dd HH:mm:ss",
    targetDateFormat: String = "dd MMM, yyyy hh:mm a"
): String {
    if (this.isNullOrEmpty()) {
        return ""
    }
    val date = createDate(sourceDateFormat)
    return date.getStringDate(targetDateFormat)
}


fun Date?.getStringDate(format: String? = "yyyy-MM-dd"): String {
    if (this == null || format == null || format.isEmpty()) {
        return ""
    }
    val outputDateFormat = SimpleDateFormat(format, Locale.getDefault())
    return outputDateFormat.format(this)
}


fun String?.createDate(sourceFormat: String = "yyyy-MM-dd", isUTCToLocal: Boolean = true): Date {
    if (this.isNullOrEmpty()) {
        return Date()
    }
    val inputDateFromat = SimpleDateFormat(sourceFormat, Locale.getDefault())

    var date = Date()
    if (isUTCToLocal) {
        inputDateFromat.timeZone = TimeZone.getTimeZone("UTC")
        date = inputDateFromat.parse(this)
        inputDateFromat.timeZone = TimeZone.getDefault()
        val formattedDate: String = inputDateFromat.format(date)

        date = inputDateFromat.parse(formattedDate)
    } else {
        try {
            date = inputDateFromat.parse(this)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }


    return date
}


fun Long?.createDate(sourceFormat: String = "yyyy-MM-dd"): Date {
    val inputDateFromat = SimpleDateFormat(sourceFormat, Locale.getDefault())
    var date = Date()
    if (this == null) {
        try {
            date = inputDateFromat.parse(Date(this).toString())
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    return date
}

@SuppressLint("SimpleDateFormat")
fun Long?.getDateFromUTCTimestamp(mDateFormat: String?, isUTCToLocal: Boolean = true): Date {

    val cal: Calendar = Calendar.getInstance(Locale.ENGLISH)
    cal.timeInMillis = this!!
    val date: String = android.text.format.DateFormat.format(mDateFormat, cal).toString()

    return date.createDate(mDateFormat!!, isUTCToLocal)
}

fun Date?.getTimeAgo(): String {
    var getDate = ""
    try {

        val now = Date().time.getDateFromUTCTimestamp("yyyy-MM-dd mm:hh:ss")

        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(now.time - this!!.time)
        val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(now.time - this.time)
        val hours: Long = TimeUnit.MILLISECONDS.toHours(now.time - this.time)
        val days: Long = TimeUnit.MILLISECONDS.toDays(now.time - this.time)


        getDate = when {
            seconds < 5 -> {
                "Just Now"
            }

            seconds < 60 -> {
                "$seconds seconds ago"
            }

            minutes < 60 -> {
                "$minutes minutes ago"
            }

            hours < 24 -> {
                if (hours.toInt() >= 8) {
                    "Today"
                } else {
                    "$hours hours ago"
                }
            }

            days >= 7 -> {
                when {
                    days > 360 -> {
                        "${(days / 360)} ${
                            if ((days / 360) > 1) {
                                "Years"
                            } else {
                                "Year"
                            }
                        } ago"
                    }

                    days > 30 -> {
                        "${(days / 30)} ${
                            if ((days / 30) > 1) {
                                "Months"
                            } else {
                                "Month"
                            }
                        } ago"
                    }

                    else -> {
                        "${((days / 7))} ${
                            if (((days / 7)) > 1) {
                                "Weeks"
                            } else {
                                "Week"
                            }
                        } ago"
                    }
                }

            }

            else -> {
                "$days ${
                    if (days > 1) {
                        "days"
                    } else {
                        "day"
                    }
                } ago"
            }


        }


    } catch (j: Exception) {
        j.printStackTrace()
    }

    return getDate
}

fun Date?.getNotificationTimeAgo(): String {
    var getDate = ""
    try {

        val now = Date()
        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(now.time - this!!.time)
        val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(now.time - this.time)
        val hours: Long = TimeUnit.MILLISECONDS.toHours(now.time - this.time)
        val days: Long = TimeUnit.MILLISECONDS.toDays(now.time - this.time)


        getDate = when {
            seconds < 60 -> {
                "Today"
            }

            minutes < 60 -> {
                "Today"
            }

            hours < 24 -> {
                "Today"
            }

            days >= 7 -> {
                if (days > 360) {
                    if ((days / 360) > 1) {
                        "${(days / 360)} Years ago"
                    } else {
                        "Last Year"
                    }

                } else if (days > 30) {
                    if ((days / 30) > 1) {
                        "${(days / 30)} Months ago"
                    } else {
                        "Last Month"
                    }
                } else {
                    if (((days / 7)) > 1) {
                        "${((days / 7))} Weeks ago"
                    } else {
                        "Last Week"
                    }
                }

            }

            else -> {
                "$days ${
                    if (days > 1) {
                        "This week"
                    } else {
                        "Yesterday"
                    }
                }"
            }


        }


    } catch (j: Exception) {
        j.printStackTrace()
    }

    return getDate
}


fun String?.getChatDate(
    sourceDateFormat: String? = "yyyy-MM-dd HH:mm:ss",
    targetDateFormat: String? = "dd MMM, yyyy"
): String {
    val date = createDate(sourceDateFormat ?: "yyyy-MM-dd HH:mm:ss")
    val cal = Calendar.getInstance()
    val selectedData = Calendar.getInstance().apply {
        time = date
    }
    if (cal.get(Calendar.DATE) == selectedData.get(Calendar.DATE) && cal.get(Calendar.YEAR) == selectedData.get(
            Calendar.YEAR
        ) && cal.get(Calendar.MONTH) == selectedData.get(Calendar.MONTH)
    ) {
        return "Today"
    } else {
        return changeDateFormat(
            sourceDateFormat ?: "yyyy-MM-dd HH:mm:ss",
            targetDateFormat ?: "dd MMM, yyyy"
        )
    }
}


fun Date?.getTimeLeft(): String {
    var getDate = ""
    try {

        val now = Date()
        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(this!!.time - now.time)
        val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(this.time - now.time)
        val hours: Long = TimeUnit.MILLISECONDS.toHours(this.time - now.time)
        val days: Long = TimeUnit.MILLISECONDS.toDays(this.time - now.time)



        getDate = when {
            seconds < 60 -> {
                "$seconds seconds"
            }

            minutes < 60 -> {
                "$minutes minutes"
            }

            hours < 24 -> {
                "$hours hours"
            }

            days >= 7 -> {
                when {
                    days >= 360 -> {
                        "${(days / 360)} ${
                            if ((days / 360) > 1) {
                                "Years"
                            } else {
                                "Year"
                            }
                        }"
                    }

                    days >= 29 -> {
                        "${(days / 29)} ${
                            if ((days / 29) > 1) {
                                "Months"
                            } else {
                                "Month"
                            }
                        }"
                    }

                    else -> {
                        "${((days / 7))} ${
                            if (((days / 7)) > 1) {
                                "Weeks"
                            } else {
                                "Week"
                            }
                        }"
                    }
                }

            }

            else -> {
                "$days ${
                    if (days > 1) {
                        "days"
                    } else {
                        "day"
                    }
                }"
            }


        }


    } catch (j: Exception) {
        j.printStackTrace()
    }

    return getDate
}


@SuppressLint("SimpleDateFormat")
fun String?.getMilliseconds(sourceFormat: String = "yyyy-MM-dd HH:mm:ss"): Long {
    var time1 = this
    try {
        val format = SimpleDateFormat(sourceFormat)
        val date1 = time1?.let { format.parse(it) }

        return date1!!.time
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        try {
            time1 = time1?.replace("T", " ")
            time1 = time1?.split("\\.".toRegex())!!.toTypedArray()[0]
            val format =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date1 = format.parse(time1)
            return date1.time
        } catch (e1: java.lang.Exception) {
            e1.printStackTrace()
        }
    }
    return 60000
}


@SuppressLint("SimpleDateFormat")
fun getTimeDifference(
    time1: String?,
    time2: String?,
    sourceFormat: String = "yyyy-MM-dd HH:mm:ss"
): Long {
    try {
        val format = SimpleDateFormat(sourceFormat)
        val date1 = format.parse(time1)
        val date2 = format.parse(time2)
        return 120000 - if (abs(date1.time - date2.time) < 120000) abs(date1.time - date2.time) else 0
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return 120000
}


@SuppressLint("SimpleDateFormat")
fun getDelayMillisAfterCurrentTime(
    delayMillis: Long,
    sourceFormat: String = "yyyy-MM-dd HH:mm:ss"
): String? {
    val foo = System.currentTimeMillis() + delayMillis
    val date = Date(foo)
    val formatter: DateFormat = SimpleDateFormat(sourceFormat)
    return formatter.format(date)
}

@SuppressLint("SimpleDateFormat")
fun getCurrentTime(sourceFormat: String = "yyyy-MM-dd HH:mm:ss"): String? {
    val foo = System.currentTimeMillis()
    val date = Date(foo)
    val formatter: DateFormat = SimpleDateFormat(sourceFormat)
    return formatter.format(date)
}

@SuppressLint("SimpleDateFormat")
fun getCurrentDate(sourceFormat: String = "yyyy-MM-dd HH:mm:ss"): Date {
    val foo = System.currentTimeMillis()
    val date = Date(foo)
    val formatter: DateFormat = SimpleDateFormat(sourceFormat)
    return formatter.format(date).createDate(sourceFormat)
}


@SuppressLint("SimpleDateFormat")
fun Date.delayDays(): Int {

    val today = Date()
    val diff = today.time - this.time
    val numOfDays = (diff / (1000 * 60 * 60 * 24)).toInt()
    val hours = (diff / (1000 * 60 * 60)).toInt()
    val minutes = (diff / (1000 * 60)).toInt()
    val seconds = (diff / 1000).toInt()
    return numOfDays
}



