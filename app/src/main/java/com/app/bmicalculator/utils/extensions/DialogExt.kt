package com.app.bmicalculator.utils.extensions


import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.view.View
import androidx.core.content.ContextCompat
import com.app.bmicalculator.R
import java.util.Calendar


fun View.openTimePickerDialog(
    calendar: Calendar = Calendar.getInstance(),
    is24Hour: Boolean = false,
    onTimeSelect: (calendar: Calendar) -> Unit
) {
    val timePickerDialog = TimePickerDialog(
        this.context, { view, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            onTimeSelect(calendar)
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), is24Hour
    )

    timePickerDialog.show()
    timePickerDialog.getButton(Dialog.BUTTON_NEGATIVE).setBackgroundResource(0)
    timePickerDialog.getButton(Dialog.BUTTON_NEGATIVE)
        .setTextColor(ContextCompat.getColor(this.context, R.color.colorPrimary))
    timePickerDialog.getButton(Dialog.BUTTON_POSITIVE).setBackgroundResource(0)
    timePickerDialog.getButton(Dialog.BUTTON_POSITIVE)
        .setTextColor(ContextCompat.getColor(this.context, R.color.colorPrimary))

}


fun showLocationSettingsAlert(mContext: Context) {
    var locationAlertDialog: AlertDialog? = null
    try {
        if (!(mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            ) && !(mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(
                LocationManager.GPS_PROVIDER
            )
        ) {

            val alertDialogPrepare = AlertDialog.Builder(mContext)

            // Setting Dialog Title
            alertDialogPrepare.setTitle(mContext.resources.getString(R.string.location_setting))
            alertDialogPrepare.setCancelable(false)

            // Setting Dialog Message
            alertDialogPrepare.setMessage(mContext.resources.getString(R.string.location_setting_enable_message))

            // On pressing Settings button
            alertDialogPrepare.setPositiveButton(
                "Settings"
            ) { dialog, which ->
                dialog.dismiss()
                try {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    mContext.startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            locationAlertDialog = alertDialogPrepare.create()
            if (locationAlertDialog != null && locationAlertDialog.isShowing) {
                locationAlertDialog.dismiss()
            }
            // Showing Alert Message
            locationAlertDialog.show()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }


}

/**
 * Function to show settings alert dialog
 * On pressing Settings button will lauch Settings Options
 */



