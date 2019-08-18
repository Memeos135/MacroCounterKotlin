package com.example.macrocounterremaster.webServices.responses

class RegisterResponseModel {
    private var code: String = ""

    fun setCode(code: String){
        this.code = code
    }

    fun getCode(): String{
        return code
    }
}