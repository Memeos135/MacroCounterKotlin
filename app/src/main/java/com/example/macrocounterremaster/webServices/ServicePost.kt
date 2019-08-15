package com.example.macrocounterremaster.webServices

import android.app.Activity
import com.example.macrocounterremaster.R
import com.example.macrocounterremaster.utils.Constants
import com.example.macrocounterremaster.utils.ErrorMapCreator
import com.example.macrocounterremaster.webServices.requests.LoginRequestModel
import com.example.macrocounterremaster.webServices.responses.LoginResponseModel
import com.google.gson.Gson
import okhttp3.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

class ServicePost {
    companion object {
        private fun error(responseCode: Int, activity: Activity):LoginResponseModel{
            val loginResponseModel = LoginResponseModel()
            loginResponseModel.setCode(responseCode.toString() + " - " + ErrorMapCreator.getHashMap(activity)[responseCode.toString()])
            return loginResponseModel
        }

        fun doPostToken(loginRequestModel: LoginRequestModel, login: Boolean, activity: Activity): LoginResponseModel {

            return try {
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

                if (response.code() == 200 && !response.body()!!.string().contains(Constants.MESSAGE)) {
                    return Gson().fromJson(response.body()!!.string(), LoginResponseModel::class.java)
                } else if (response.code() == 200 && response.body()!!.string().contains(Constants.MESSAGE)) {
                    error(response.code(), activity)
                } else {
                    error(response.code(), activity)
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