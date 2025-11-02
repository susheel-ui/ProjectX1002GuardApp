package com.example.project_b_security_gardapp.api.Repo

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.project_b_security_gardapp.api.Entities.User
import com.example.project_b_security_gardapp.api.Entities.userLoginEntity
import com.example.project_b_security_gardapp.api.Responses.UserLoginResponse
import com.example.project_b_security_gardapp.api.Services.UserServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

import java.io.File

class UserRepository(private val userServices: UserServices) {
    private val loginData = MutableLiveData<UserLoginResponse>()
    private val userData = MutableLiveData<User>()

    val UserDataLiveData: LiveData<User>
        get() = userData
    val loginLiveData: LiveData<UserLoginResponse>
        get() = loginData

    suspend fun login(body: userLoginEntity): Response<UserLoginResponse> {
        // Make the API call
        val result = userServices.login(body)

        // Check if the call was successful and the body is not null
//        if(result.body() != null){
//            // Post the received data to the LiveData object
//            loginData.postValue(result.body())
//        }
        return result
    }

    suspend fun GetUserBytoken(token: String): Response<User> {
        val result = userServices.getDetailsByToken("Bearer $token")
//        if(result.body() != null){
//            // Post the received data to the LiveData object
//            userData.postValue(result.body())
//        }
        return result
    }

    suspend fun sendGuestRequest(
        token: String,
        guestName: String,
        phoneNumber: String,
        description: String,
        ownerId: String,
        photoFile1: File?,
        photoFile2: File?
    ): Int {
        return try {
            val guestNameBody = guestName.toRequestBody("text/plain".toMediaType())
            val phoneBody = phoneNumber.toRequestBody("text/plain".toMediaType())
            val descBody = description.toRequestBody("text/plain".toMediaType())
            val ownerBody = ownerId.toRequestBody("text/plain".toMediaType())

            // Optional photo parts
            val photoPart1 = photoFile1?.let {
                val requestFile = it.asRequestBody("image/jpeg".toMediaType())
                MultipartBody.Part.createFormData("photo1", it.name, requestFile)
            }

            val photoPart2 = photoFile2?.let {
                val requestFile = it.asRequestBody("image/jpeg".toMediaType())
                MultipartBody.Part.createFormData("photo2", it.name, requestFile)
            }

            // ✅ Make network call
            val result = userServices.sendGuestRequest(
                "Bearer $token",
                guestName = guestNameBody,
                phoneNumber = phoneBody,
                description = descBody,
                ownerId = ownerBody,
                photo1 = photoPart1,
                photo2 = photoPart2
            )

            Log.d(TAG, "sendGuestRequest: ${result.body()} and code is :- ${result.code()}")
            result.code() // ✅ return the actual HTTP code
        } catch (e: Exception) {
            Log.e(TAG, "sendGuestRequest failed: ${e.message}")
            500 // return fallback error code
        }
    }
}