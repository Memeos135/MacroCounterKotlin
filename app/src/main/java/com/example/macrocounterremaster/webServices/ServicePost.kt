package com.example.macrocounterremaster.webServices

import android.app.Activity
import com.example.macrocounterremaster.R
import com.example.macrocounterremaster.utils.Constants
import com.example.macrocounterremaster.utils.ErrorMapCreator
import com.example.macrocounterremaster.webServices.requests.LoginRequestModel
import com.example.macrocounterremaster.webServices.requests.RegisterRequestModel
import com.example.macrocounterremaster.webServices.responses.AuthenticationResponseModel
import com.google.gson.Gson
import okhttp3.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

class ServicePost {
    companion object {
        private fun error(responseCode: Int, activity: Activity):String{
            return responseCode.toString() + " - " + ErrorMapCreator.getHashMap(activity)[responseCode.toString()]
        }

        fun doPostRegister(registerRequestModel: RegisterRequestModel, activity: Activity): AuthenticationResponseModel{
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
                    Gson().fromJson(result, AuthenticationResponseModel::class.java)

                    // if registration is not successful > return error code
                } else if (response.code() == 200 && result.contains(Constants.MESSAGE)) {
                    val registerResponseModel = AuthenticationResponseModel(null, error(response.code(), activity), null, null, null, null, null, null, null)
                    registerResponseModel

                    // if registration is not successful due to network issues > return default error code
                } else {
                    val registerResponseModel = AuthenticationResponseModel(null, error(response.code(), activity), null, null, null, null, null, null, null)
                    registerResponseModel
                }

            } catch (e: Exception) {
                e.printStackTrace()
                return AuthenticationResponseModel(null, ErrorMapCreator.getHashMap(activity)[Constants.ZERO].toString(), null, null, null, null, null, null, null)
            }
        }

        fun doPostToken(loginRequestModel: LoginRequestModel, login: Boolean, activity: Activity): AuthenticationResponseModel {

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
                    Gson().fromJson(result, AuthenticationResponseModel::class.java)

                } else if (response.code() == 200 && result.contains(Constants.MESSAGE)) {
                    val loginResponseModel = AuthenticationResponseModel(null, error(response.code(), activity), null, null, null, null, null, null, null)
                    loginResponseModel

                } else {
                    val loginResponseModel = AuthenticationResponseModel(null, error(response.code(), activity), null, null, null, null, null, null, null)
                    loginResponseModel
                }

            } catch (e: Exception) {
                e.printStackTrace()
                return AuthenticationResponseModel(null, ErrorMapCreator.getHashMap(activity)[Constants.ZERO].toString(), null, null, null, null, null, null, null)
            }
        }
    }
}