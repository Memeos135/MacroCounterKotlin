package com.example.macrocounterremaster.webServices.requests

import android.app.Activity
import com.example.macrocounterremaster.R

class UpdateMacrosRequestModel(private val category: String, private val updatedValue: String, private val email: String, private val password: String, private val date: String, private val activity: Activity) {
    fun getPostFormat(): String{
        return "{ \"category\": \"${category}\", \"updatedValue\": \"${updatedValue}\", \"email\": \"${email}\", \"password\": \"${password}\", \"date\": \"${date}\"}"
    }

    fun getPostURL(): String{
        return activity.getString(R.string.update_macros_url)
    }
}