package com.example.project_b_security_gardapp.api.Retrofit

import com.example.project_b_security_gardapp.api.Services.UserServices
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val getInstance by lazy {
        Retrofit.Builder().baseUrl("https://gateguard.cloud/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
//    https://gateguard.cloud/
//    http://192.168.29.160:8080/   10.0.2.2ipconfig

}