package com.example.macrocounterremaster.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class NoteModel() : Parcelable {
    var month: String = ""
    var day: String = ""
    var description: String = ""

    constructor(month: String, day: String, description: String): this(){
        this.month = month
        this.day = day
        this.description = description
    }
}