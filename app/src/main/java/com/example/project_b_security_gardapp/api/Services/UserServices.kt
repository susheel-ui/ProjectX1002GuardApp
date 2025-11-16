package com.example.project_b_security_gardapp.api.Services

import com.example.project_b_security_gardapp.api.Entities.GuestRequestResponse
import com.example.project_b_security_gardapp.api.Entities.RequestsResultEntity
import com.example.project_b_security_gardapp.api.Entities.SignUpUserEntity
import com.example.project_b_security_gardapp.api.Entities.User
import com.example.project_b_security_gardapp.api.Entities.userLoginEntity
import com.example.project_b_security_gardapp.api.Responses.UserLoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface UserServices {
        @POST("/api/auth/login")
        suspend fun login(@Body loginEntity: userLoginEntity):Response<UserLoginResponse>

        @POST("/api/auth/register/guard")
        suspend fun SignUp(@Body body: SignUpUserEntity):Response<UserLoginResponse>

        @GET("/api/user/profile")
        suspend fun getDetailsByToken(@Header("Authorization") token: String):Response<User>

        @Multipart
        @POST("/api/guard/request")
        suspend fun sendGuestRequest(
                @Header("Authorization") token: String,
                @Part("guestName") guestName: RequestBody,
                @Part("phoneNumber") phoneNumber: RequestBody,
                @Part("description") description: RequestBody,
                @Part("flatNumber") ownerId: RequestBody,
                @Part photo1: MultipartBody.Part?,
                @Part photo2: MultipartBody.Part?
        ): Response<ResponseBody>

        @GET("/api/guard/requests")
        suspend fun getAllGuestRequests(
                @Header("Authorization") token: String
        ): Response<List<RequestsResultEntity>>

        @GET("/api/user/visitor-requests/{id}")
        suspend fun getRequestById(
                @Header("Authorization") token: String,
                @Path("id") id:Int
        ):Response<RequestsResultEntity>

        @PUT("/api/guard/visitor-requests/{id}/checkout")
        suspend fun VisitorCheckOut(
                @Header("Authorization") token: String,
                @Path("id") id:Int
        ):Response<RequestsResultEntity>

        @PUT("/api/guard/visitor-requests/{id}/checkin")
        suspend fun VisitorCheckIn(
                @Header("Authorization") token: String,
                @Path("id") id:Int
        ):Response<RequestsResultEntity>





}