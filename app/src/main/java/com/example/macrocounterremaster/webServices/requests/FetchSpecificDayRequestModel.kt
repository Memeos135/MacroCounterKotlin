package com.example.macrocounterremaster.webServices.requests

import android.app.Activity
import com.example.macrocounterremaster.R

class FetchSpecificDayRequestModel(private val email: String?, private val password: String?, private val year: String?, private val month: String?, private val day: String?, private val activity: Activity?){
    fun getPostFormat(): String{
        return "{ \"email\": \"${email}\", \"password\": \"${password}\", \"year\": \"${year}\", \"month\": \"${month}\", \"day\": \"${day}\"}"
    }

    fun getEmail(): String?{
        return email
    }

    fun getPassword(): String?{
        return password
    }

    fun getYear(): String?{
        return year
    }

    fun getMonth(): String?{
        return month
    }

    fun getDay(): String?{
        return day
    }

    fun getURL(): String{
        return activity!!.getString(R.string.fetch_specific_day)
    }
}