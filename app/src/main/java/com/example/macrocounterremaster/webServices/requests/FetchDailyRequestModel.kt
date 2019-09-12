package com.example.macrocounterremaster.webServices.requests

import android.app.Activity
import com.example.macrocounterremaster.R

class FetchDailyRequestModel(private val email: String, private val password: String, private val activity: Activity) {
    fun getPostFormat(): String{
        return "{ \"email\": \"${email}\", \"password\": \"${password}\"}"
    }

    fun getEmail(): String{
        return email
    }

    fun getPassword(): String{
        return password
    }

    fun getURL(): String{
        return activity.getString(R.string.fetch_daily_url)
    }
}