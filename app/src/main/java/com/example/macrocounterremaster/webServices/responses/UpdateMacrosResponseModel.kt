package com.example.macrocounterremaster.webServices.responses

class UpdateMacrosResponseModel(private val code: String?, private val response: String?) {
    fun getCode(): String?{
        return code
    }

    fun getResponse(): String?{
        return response
    }
}