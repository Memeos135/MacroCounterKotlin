package com.example.macrocounterremaster.webServices.requests

import android.app.Activity
import com.example.macrocounterremaster.R

class FetchDailyRequestModel(private val email: String, private val password: String, private val token: String, private val activity: Activity) {
    fun getPostFormat(): String{
        return "{ \"email\": \"${email}\", \"password\": \"${password}\", \"token\": \"${token}\"}"
    }

    fun getEmail(): String{
        return email
    }

    fun getPassword(): String{
        return password
    }

    fun getToken(): String{
        return token
    }

    fun getURL(): String{
        return activity.getString(R.string.fetch_daily_url)
    }
}