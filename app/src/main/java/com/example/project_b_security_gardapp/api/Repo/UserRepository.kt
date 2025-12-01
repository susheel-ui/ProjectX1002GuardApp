package com.example.project_b_security_gardapp.api.Repo

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.project_b_security_gardapp.api.Entities.GuestRequestResponse
import com.example.project_b_security_gardapp.api.Entities.RequestsResultEntity
import com.example.project_b_security_gardapp.api.Entities.SignUpUserEntity
import com.example.project_b_security_gardapp.api.Entities.User
import com.example.project_b_security_gardapp.api.Entities.userLoginEntity
import com.example.project_b_security_gardapp.api.Responses.ResponseStaffArrayItem
import com.example.project_b_security_gardapp.api.Responses.TodayStaffEntity
import com.example.project_b_security_gardapp.api.Responses.UserLoginResponse
import com.example.project_b_security_gardapp.api.Services.UserServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
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
    suspend fun SignUp(body:SignUpUserEntity):Response<UserLoginResponse>{
        return userServices.SignUp(body)
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
    ): Response<ResponseStaffArrayItem> {


            val guestNameBody = guestName.toRequestBody("text/plain".toMediaType())
            val phoneBody = phoneNumber.toRequestBody("text/plain".toMediaType())
            val descBody = description.toRequestBody("text/plain".toMediaType())
            val ownerBody = ownerId.toRequestBody("text/plain".toMediaType())

//

            val photoPart1 = photoFile1?.let {
                val req = it.asRequestBody("image/*".toMediaType())
                MultipartBody.Part.createFormData("photo1", it.name, req)

            }

            val photoPart2 = photoFile2?.let {
                val req = it.asRequestBody("image/*".toMediaType())
                MultipartBody.Part.createFormData("photo2", it.name, req)
            }



            return userServices.sendGuestRequest(
                "Bearer $token",
                guestNameBody,
                phoneBody,
                descBody,
                ownerBody,
                photoPart1,
                photoPart2
            )
    }



    suspend fun getAllGuestRequests(token: String): Response<List<RequestsResultEntity>> {
        return userServices.getAllGuestRequests(token)
    }

    suspend fun getRequestById(id:String,token: String): Response<RequestsResultEntity> {
        return userServices.getRequestById(token,id.toInt())
    }
    suspend fun VisitorCheckIn(id:String,token: String): Response<RequestsResultEntity> {
        return userServices.VisitorCheckIn(token,id.toInt())
    }
    suspend fun VisitorCheckOut(id:String,token: String): Response<RequestsResultEntity> {
        return userServices.VisitorCheckOut(token,id.toInt())
    }

    suspend fun getStaff(token:String): Response<List<ResponseStaffArrayItem>>{
        return userServices.getStaff(token)
    }
    suspend fun startAttendanceOfStaff(staffCode:Int,token: String): Response<ResponseBody> {
        return userServices.startAttendanceOfStaff(staffCode,token)
    }
    suspend fun getAllTodayStaffAttendance(token: String): Response<List<TodayStaffEntity>>{
        return userServices.getAllTodayAttendancesStarted(token)
    }
    suspend fun staffCheckIn(id:Int,token: String):Response<ResponseBody>{
        return userServices.StaffCheckIn(id,token)
    }
    suspend fun staffCheckOut(id:Int,token: String):Response<ResponseBody>{
        return userServices.StaffCheckOut(id,token)
    }


}