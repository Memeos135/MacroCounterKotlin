package com.example.macrocounterremaster.webServices.responses

class LoginResponseModel{
    private var code: String = ""
    private var id: String = ""

    fun setCode(code: String){
        this.code = code
    }

    fun getCode(): String{
        return code
    }

    fun setId(id: String){
        this.id = id
    }

    fun getId(): String{
        return id
    }
}