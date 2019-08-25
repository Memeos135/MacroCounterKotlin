package com.example.macrocounterremaster.webServices.responses

class RegisterResponseModel {
    private var id: String = ""
    private var code: String = ""

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