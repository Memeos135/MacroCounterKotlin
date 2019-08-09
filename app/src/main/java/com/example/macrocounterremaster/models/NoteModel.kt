package com.example.macrocounterremaster.models

class NoteModel() {
    var month: String = ""
    var day: String = ""
    var description: String = ""

    constructor(month: String, day: String, description: String): this(){
        this.month = month
        this.day = day
        this.description = description
    }
}