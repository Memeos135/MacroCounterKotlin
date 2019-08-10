package com.example.macrocounterremaster.helpers

import android.app.Activity
import android.app.ProgressDialog

class ProgressDialogHelper {
    companion object{
        fun getProgressDialog(activity: Activity, msg: Int): ProgressDialog {
            val dialog = ProgressDialog(activity)
            dialog.setMessage(activity.getString(msg))
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            return dialog
        }
    }
}