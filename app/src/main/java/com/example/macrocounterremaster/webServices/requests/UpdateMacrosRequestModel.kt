package com.example.macrocounterremaster.webServices.requests

import android.app.Activity
import com.example.macrocounterremaster.R

class UpdateMacrosRequestModel(private val category: String, private val updatedValue: String, private val activity: Activity) {
    fun getPostFormat(): String{
        return "{ \"category\": \"${category}\", \"updatedValue\": \"${updatedValue}\"}"
    }

    fun getPostURL(): String{
        return activity.getString(R.string.update_macros_url)
    }
}