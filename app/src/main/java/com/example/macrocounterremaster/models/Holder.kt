package com.example.macrocounterremaster.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Holder(
    private var protein_progress: String?,
    private var protein_goal: String?,
    private var carbs_progress: String?,
    private var carbs_goal: String?,
    private var fats_progress: String?,
    private var fats_goal: String?,
    private var calorie_progress: String?,
    private var calorie_goal: String?,
    private var list: ArrayList<NoteModel>
) : Parcelable {

    fun getList(): ArrayList<NoteModel>{
        return list
    }

    fun getProteinCurrent(): String{
        return protein_progress.toString()
    }

    fun getProteinRemain(): String{
        return protein_goal.toString()
    }

    fun getCarbsCurrent(): String{
        return carbs_progress.toString()
    }

    fun getCarbsRemain(): String{
        return carbs_goal.toString()
    }

    fun getFatsCurrent(): String{
        return fats_progress.toString()
    }

    fun getFatsRemain(): String{
        return fats_goal.toString()
    }

    fun getCalCurrent(): String{
        return calorie_progress.toString()
    }

    fun getCalRemain(): String{
        return calorie_goal.toString()
    }
}