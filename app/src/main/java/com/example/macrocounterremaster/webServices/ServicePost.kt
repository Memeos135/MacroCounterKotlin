package com.example.macrocounterremaster.webServices

import android.app.Activity
import com.example.macrocounterremaster.R
import com.example.macrocounterremaster.utils.Constants
import com.example.macrocounterremaster.utils.ErrorMapCreator
import com.example.macrocounterremaster.webServices.requests.LoginRequestModel
import com.example.macrocounterremaster.webServices.requests.RegisterRequestModel
import com.example.macrocounterremaster.webServices.responses.LoginResponseModel
import com.example.macrocounterremaster.webServices.responses.RegisterResponseModel
import com.google.gson.Gson
import okhttp3.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

class ServicePost {
    companion object {
        private fun error(responseCode: Int, activity: Activity):String{
            return responseCode.toString() + " - " + ErrorMapCreator.getHashMap(activity)[responseCode.toString()]
        }

        fun doPostRegister(registerRequestModel: RegisterRequestModel, activity: Activity): RegisterResponseModel{
            try {
                val client: OkHttpClient = OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build()

                val body: RequestBody = RequestBody.create(
                    MediaType.parse(activity.getString(R.string.x_urlencoded)),
                    registerRequestModel.getPostFormat()
                )
                val requestBuilder: Request.Builder = Request.Builder()
                    .url(registerRequestModel.url)
                    .post(body)

                val response: Response = client.newCall(requestBuilder.build()).execute()

                val result = response.body()!!.string()

                return if (response.code() == 200 && !result.contains(Constants.MESSAGE)) {
                    // if registration is successful > return token
                    val registerResponseModel = RegisterResponseModel()
                    registerResponseModel.setId(result)
                    registerResponseModel

                    // if registration is not successful > return error code
                } else if (response.code() == 200 && result.contains(Constants.MESSAGE)) {
                    val registerResponseModel = RegisterResponseModel()
                    registerResponseModel.setCode(error(response.code(), activity))
                    registerResponseModel

                    // if registration is not successful due to network issues > return default error code
                } else {
                    val registerResponseModel = RegisterResponseModel()
                    registerResponseModel.setCode(error(response.code(), activity))
                    registerResponseModel
                }

            } catch (e: Exception) {
                e.printStackTrace()
                val registerResponseModel = RegisterResponseModel()
                registerResponseModel.setCode(ErrorMapCreator.getHashMap(activity)[Constants.ZERO].toString())
                return registerResponseModel
            }
        }

        fun doPostToken(loginRequestModel: LoginRequestModel, login: Boolean, activity: Activity): LoginResponseModel {

            try {
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

                if (!login) {
                    requestBuilder.addHeader(
                        activity.getString(R.string.authorization),
                        activity.getString(R.string.bearer) + " " + loginRequestModel.getAccessToken()
                    )
                }

                val response: Response = client.newCall(requestBuilder.build()).execute()

                val result = response.body()!!.string()

                return if (response.code() == 200 && !result.contains(Constants.MESSAGE)) {
                    Gson().fromJson(response.body()!!.string(), LoginResponseModel::class.java)

                } else if (response.code() == 200 && result.contains(Constants.MESSAGE)) {
                    val loginResponseModel = LoginResponseModel()
                    loginResponseModel.setCode(error(response.code(), activity))
                    loginResponseModel

                } else {
                    val loginResponseModel = LoginResponseModel()
                    loginResponseModel.setCode(error(response.code(), activity))
                    loginResponseModel
                }

            } catch (e: Exception) {
                e.printStackTrace()
                val loginResponseModel = LoginResponseModel()
                loginResponseModel.setCode(ErrorMapCreator.getHashMap(activity)[Constants.ZERO].toString())
                return loginResponseModel
            }
        }
    }
}