package com.example.macrocounterremaster.webServices.requests

import com.example.macrocounterremaster.models.FullAuthenticationValues

class RegisterRequestModel(private val fullAuthenticationValues: FullAuthenticationValues, val url: String){
    fun getPostFormat(): String{
        return "{ \"name\": \"${fullAuthenticationValues.name}\", \"email\": \"${fullAuthenticationValues.email}\", \"password\": \"${fullAuthenticationValues.password}\", \"protein\": \"${fullAuthenticationValues.protein}\", \"carbohydrates\": \"${fullAuthenticationValues.carbs}\", \"fats\": \"${fullAuthenticationValues.fats}\" }"
    }

    fun getEmail(): String{
        return fullAuthenticationValues.email
    }

    fun getPassword(): String{
        return fullAuthenticationValues.password
    }
}