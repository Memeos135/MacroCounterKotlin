package com.example.macrocounterremaster.webServices.requests

import com.example.macrocounterremaster.models.FullValues

class RegisterRequestModel(private val fullValues: FullValues, val url: String){
    fun getPostFormat(): String{
        return "{ \"name\": \"${fullValues.name}\", \"email\": \"${fullValues.email}\", \"password\": \"${fullValues.password}\", \"protein\": \"${fullValues.protein}\", \"carbohydrates\": \"${fullValues.carbs}\", \"fats\": \"${fullValues.fats}\" }"
    }

    fun getEmail(): String{
        return fullValues.email
    }

    fun getPassword(): String{
        return fullValues.password
    }
}