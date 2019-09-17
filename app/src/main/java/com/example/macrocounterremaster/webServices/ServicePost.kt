package com.example.macrocounterremaster.webServices

import android.app.Activity
import android.preference.PreferenceManager
import com.example.macrocounterremaster.R
import com.example.macrocounterremaster.helpers.SaveHelper
import com.example.macrocounterremaster.utils.Constants
import com.example.macrocounterremaster.utils.ErrorMapCreator
import com.example.macrocounterremaster.webServices.requests.FetchDailyRequestModel
import com.example.macrocounterremaster.webServices.requests.LoginRequestModel
import com.example.macrocounterremaster.webServices.requests.RegisterRequestModel
import com.example.macrocounterremaster.webServices.requests.UpdateMacrosRequestModel
import com.example.macrocounterremaster.webServices.responses.AuthenticationResponseModel
import com.example.macrocounterremaster.webServices.responses.FetchDailyProgressResponseModel
import com.example.macrocounterremaster.webServices.responses.UpdateMacrosResponseModel
import com.google.gson.Gson
import okhttp3.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

class ServicePost {
    companion object {
        private fun error(responseCode: Int, activity: Activity):String{
            return responseCode.toString() + " - " + ErrorMapCreator.getHashMap(activity)[responseCode.toString()]
        }

        fun doPostUpdateDaily(updateValueRequestModel: UpdateMacrosRequestModel, activity: Activity): UpdateMacrosResponseModel{
            try{
                val client: OkHttpClient = OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build()

                val body: RequestBody = RequestBody.create(
                    MediaType.parse(activity.getString(R.string.x_urlencoded)),
                    updateValueRequestModel.getPostFormat()
                )
                val requestBuilder: Request.Builder = Request.Builder()
                    .url(updateValueRequestModel.getPostURL())
                    .post(body)

                val response: Response = client.newCall(requestBuilder.build()).execute()

                val result = response.body()!!.string()

                if (response.code() == 401){
                    // fetch token then retry
                    val email = PreferenceManager.getDefaultSharedPreferences(activity).getString(Constants.EMAIL, "")
                    val password = PreferenceManager.getDefaultSharedPreferences(activity).getString(Constants.PASSWORD, "")

                    val authenticationResponseModel = doPostToken(LoginRequestModel(email.toString(), password.toString(), activity.getString(R.string.token_url)), true, activity)

                    return if(authenticationResponseModel.getCode() == null){
                        // save token
                        val id = authenticationResponseModel.getId()
                        SaveHelper.saveToken(id!!, activity)
                        // doPostUpdateDaily again
                        doPostUpdateDaily(updateValueRequestModel, activity)
                    }else{
                        return UpdateMacrosResponseModel(null, null)
                    }
                }else{
                    return if (response.code() == 200 && !result.contains(Constants.MESSAGE)) {
                        // if doPostUpdateDaily is successful > return token
                        Gson().fromJson(result, UpdateMacrosResponseModel::class.java)

                        // if doPostUpdateDaily is not successful > return error code
                    } else if (response.code() == 200 && result.contains(Constants.MESSAGE)) {
                        val updateMacrosResponseModel = UpdateMacrosResponseModel(ErrorMapCreator.getHashMap(activity)[Constants.ZERO].toString(), null)
                        updateMacrosResponseModel

                        // if doPostUpdateDaily is not successful due to network issues > return default error code
                    } else {
                        val updateMacrosResponseModel = UpdateMacrosResponseModel(error(response.code(), activity), null)
                        updateMacrosResponseModel
                    }
                }

            }catch (e: Exception){
                e.printStackTrace()
                return UpdateMacrosResponseModel(ErrorMapCreator.getHashMap(activity)[Constants.ZERO].toString(), null)
            }
        }

        fun doPostDaily(fetchDailyRequestModel: FetchDailyRequestModel, activity: Activity): FetchDailyProgressResponseModel{
            try{
                val client: OkHttpClient = OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build()

                val body: RequestBody = RequestBody.create(
                    MediaType.parse(activity.getString(R.string.x_urlencoded)),
                    fetchDailyRequestModel.getPostFormat()
                )
                val requestBuilder: Request.Builder = Request.Builder()
                    .url(fetchDailyRequestModel.getURL())
                    .post(body)

                val response: Response = client.newCall(requestBuilder.build()).execute()

                val result = response.body()!!.string()

                if (response.code() == 401){
                    // fetch token then retry
                    val authenticationResponseModel = doPostToken(LoginRequestModel(fetchDailyRequestModel.getEmail(), fetchDailyRequestModel.getPassword(), activity.getString(R.string.token_url)), true, activity)

                    return if(authenticationResponseModel.getCode() == null){
                        // save token
                        val id = authenticationResponseModel.getId()
                        SaveHelper.saveToken(id!!, activity)
                        // doPostDaily again
                        doPostDaily(FetchDailyRequestModel(fetchDailyRequestModel.getEmail(), fetchDailyRequestModel.getPassword(), activity), activity)
                    }else{
                        FetchDailyProgressResponseModel(null, null, null, authenticationResponseModel.getCode())
                    }
                }else{
                    return if (response.code() == 200 && !result.contains(Constants.MESSAGE)) {
                        // if fetching is successful > return token
                        Gson().fromJson(result, FetchDailyProgressResponseModel::class.java)

                        // if fetching is not successful > return error code
                    } else if (response.code() == 200 && result.contains(Constants.MESSAGE)) {
                        val fetchDailyProgressResponse = FetchDailyProgressResponseModel(null, null, null, error(response.code(), activity))
                        fetchDailyProgressResponse

                        // if fetching is not successful due to network issues > return default error code
                    } else {
                        val fetchDailyProgressResponse = FetchDailyProgressResponseModel(null, null, null, ErrorMapCreator.getHashMap(activity)[Constants.ZERO].toString())
                        fetchDailyProgressResponse
                    }
                }

            }catch(e: Exception){
                e.printStackTrace()
                return FetchDailyProgressResponseModel(null, null, null, ErrorMapCreator.getHashMap(activity)[Constants.ZERO].toString())
            }
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