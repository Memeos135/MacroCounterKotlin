package com.example.macrocounterremaster.webServices.requests

class LoginRequestModel() {
    private var username: String = ""
    private var password: String = ""
    private var url: String = ""

    private var accessToken: String = ""

    constructor(username: String, password: String, url: String): this(){
        this.username = username
        this.password = password
        this.url = url
    }

    fun getPostFormat(): String{
        return "{ \"email\": \"$username\",\"password\": \"$password\" }"
    }

    fun setAccessToken(access_token: String){
        this.accessToken = access_token
    }

    fun getAccessToken(): String{
        return this.accessToken
    }
}