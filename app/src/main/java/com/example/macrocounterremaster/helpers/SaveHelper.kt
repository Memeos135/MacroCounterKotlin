package com.example.macrocounterremaster.helpers

import android.app.Activity
import android.preference.PreferenceManager
import com.example.macrocounterremaster.utils.Constants

class SaveHelper {
    companion object{
        fun saveTokenAndCredentialsRegister(token: String, email: String, password: String, name: String?, activity: Activity){
            PreferenceManager.getDefaultSharedPreferences(activity).edit()
                .putString(Constants.TOKEN, token)
                .putString(Constants.EMAIL, email)
                .putString(Constants.PASSWORD, password)
                .putString(Constants.NAME, name)
                .putString(Constants.AUTO_LOGIN, Constants.AUTO_LOGIN)
                .apply()
        }

        fun saveTokenAndCredentialsLogin(token: String, email: String, password: String, activity: Activity){
            PreferenceManager.getDefaultSharedPreferences(activity).edit()
                .putString(Constants.TOKEN, token)
                .putString(Constants.EMAIL, email)
                .putString(Constants.PASSWORD, password)
                .putString(Constants.AUTO_LOGIN, Constants.AUTO_LOGIN)
                .apply()
        }

        fun removeAutoLogin(activity: Activity){
            PreferenceManager.getDefaultSharedPreferences(activity).edit()
                .putString(Constants.AUTO_LOGIN, "")
                .apply()
        }
    }
}