package com.example.macrocounterremaster.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize
import androidx.room.PrimaryKey



@Parcelize
@Entity(tableName = "notes")
class NoteModel() : Parcelable {

    @PrimaryKey(autoGenerate = true)
    private var uid: Int = 0

    @ColumnInfo(name = "month")
    private var month: String = ""

    @ColumnInfo(name = "day")
    private var day: String = ""

    @ColumnInfo(name = "description")
    private var description: String = ""

    @ColumnInfo(name = "year")
    private var year: String = ""

    constructor(month: String, day: String, year: String, description: String): this(){
        this.month = month
        this.day = day
        this.description = description
        this.year = year
    }

    fun getUid(): Int {
        return uid
    }

    fun setUid(uid: Int) {
        this.uid = uid
    }

    fun setMonth(month: String){
        this.month = month
    }

    fun setDay(day: String){
        this.day = day
    }

    fun setYear(year: String){
        this.year = year
    }

    fun getYear(): String{
        return year
    }

    fun setDescription(description: String){
        this.description = description
    }

    fun getMonth(): String{
        return month
    }

    fun getDay(): String{
        return day
    }

    fun getDescription(): String{
        return description
    }
}