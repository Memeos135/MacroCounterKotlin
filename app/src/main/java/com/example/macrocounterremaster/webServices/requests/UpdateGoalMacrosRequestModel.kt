package com.example.macrocounterremaster.webServices.requests

import android.app.Activity
import com.example.macrocounterremaster.R

class UpdateGoalMacrosRequestModel(private val protein: String, private val carbs: String, private val fats: String, private val activity: Activity) {
    fun getPostFormat(): String{
        return "{ \"protein\": \"${protein}\", \"carbs\": \"${carbs}\", \"fats\": \"${fats}\"}"
    }

    fun getPostURL(): String{
        return activity.getString(R.string.goal_update_url)
    }
}