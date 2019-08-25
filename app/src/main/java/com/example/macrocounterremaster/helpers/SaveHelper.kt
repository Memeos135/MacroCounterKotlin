package com.example.macrocounterremaster.helpers

import android.app.Activity
import android.preference.PreferenceManager
import com.example.macrocounterremaster.utils.Constants

class SaveHelper {
    companion object{
        fun saveTokenAndCredentials(token: String, email: String, password: String, activity: Activity){
            PreferenceManager.getDefaultSharedPreferences(activity).edit()
                .putString(Constants.TOKEN, token)
                .putString(Constants.EMAIL, email)
                .putString(Constants.PASSWORD, password)
                .apply()
        }
    }
}