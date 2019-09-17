package com.example.macrocounterremaster.helpers

import android.app.Activity
import android.app.Dialog
import android.view.Window
import android.view.WindowManager

class GoalDialogHelper {
    companion object{
        fun getGoalDialog(activity: Activity, layout: Int): Dialog {
            val dialog = Dialog(activity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(layout)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.window!!.setLayout (WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
            dialog.setCancelable(true)

            return dialog
        }
    }
}