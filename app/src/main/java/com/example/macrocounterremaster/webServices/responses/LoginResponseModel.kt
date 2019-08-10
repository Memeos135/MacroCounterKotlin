package com.example.macrocounterremaster.webServices.responses

class LoginResponseModel{
    private var code: String = ""

    fun setCode(code: String){
        this.code = code
    }

    fun getCode(): String{
        return code
    }
}