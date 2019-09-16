package com.example.macrocounterremaster.helpers

import android.app.Activity
import android.app.AlertDialog
import com.example.macrocounterremaster.R

class ConfirmDialogHelper {
    companion object{
        fun getConfirmDialog(activity: Activity): AlertDialog.Builder{
            val builder = AlertDialog.Builder(activity)
            // Set the alert dialog title
            builder.setTitle(R.string.logout)
            // Display a message on alert dialog
            builder.setMessage(R.string.logout_confirmation)
            return builder
        }
    }
}