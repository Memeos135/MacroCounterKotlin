package com.example.macrocounterremaster.helpers

import android.app.Activity
import android.app.Dialog
import android.view.Window
import android.view.WindowManager

class NoteDialogHelper {
    companion object {
        fun showInputDialog(activity: Activity, layout: Int): Dialog {
            val dialog = Dialog(activity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(layout)
            dialog.window!!.setLayout (WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
            dialog.setCancelable(true)

            return dialog
        }
    }
}