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

        fun saveGoalValues(activity: Activity, proteinGoal: String, carbsGoal: String, fatsGoal: String){
            PreferenceManager.getDefaultSharedPreferences(activity).edit()
                .putString(Constants.PROTEIN_GOAL, proteinGoal)
                .putString(Constants.CARBS_GOAL, carbsGoal)
                .putString(Constants.FATS_GOAL, fatsGoal)
                .apply()
        }

        fun saveToken(token: String, activity: Activity){
            PreferenceManager.getDefaultSharedPreferences(activity).edit()
                .putString(Constants.TOKEN, token)
                .apply()
        }
    }
}