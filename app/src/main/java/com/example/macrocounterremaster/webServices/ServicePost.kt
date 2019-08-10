package com.example.macrocounterremaster.webServices

import android.app.Activity
import com.example.macrocounterremaster.R
import com.example.macrocounterremaster.utils.ErrorMapCreator
import com.example.macrocounterremaster.webServices.requests.LoginRequestModel
import com.example.macrocounterremaster.webServices.responses.LoginResponseModel
import com.google.gson.Gson
import okhttp3.*
import java.util.concurrent.TimeUnit

class ServicePost {
    companion object {
        fun doPostToken(loginRequestModel: LoginRequestModel, login: Boolean, activity: Activity): LoginResponseModel {

            val client: OkHttpClient = OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            val body: RequestBody = RequestBody.create(
                MediaType.parse(activity.getString(R.string.x_urlencoded)),
                loginRequestModel.getPostFormat()
            )
            val requestBuilder: Request.Builder = Request.Builder()
                .url(activity.getString(R.string.token_url))
                .post(body)

            if(!login){
                requestBuilder.addHeader(activity.getString(R.string.authorization), activity.getString(R.string.bearer) + " " + loginRequestModel.getAccessToken())
            }

            val response: Response = client.newCall(requestBuilder.build()).execute()

            return if(response.code() == 200){
                val loginResponseModel: LoginResponseModel = Gson().fromJson(response.body()!!.string(), LoginResponseModel::class.java)
                loginResponseModel

            }else{
                val loginResponseModel = LoginResponseModel()
                loginResponseModel.setCode(response.code().toString() + " - " + ErrorMapCreator.getHashMap(activity)[response.code().toString()])
                loginResponseModel
            }
        }
    }
}