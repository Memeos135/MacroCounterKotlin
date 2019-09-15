package com.example.macrocounterremaster.helpers

import android.app.Activity
import android.content.res.Configuration


class OrientationHelper {
    companion object{
        fun isLandscape(activity: Activity): Boolean{
            val orientation = activity.resources.configuration.orientation
            return orientation == Configuration.ORIENTATION_LANDSCAPE
        }
    }
}