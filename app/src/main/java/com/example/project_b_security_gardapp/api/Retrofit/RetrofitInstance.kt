package com.example.project_b_security_gardapp.api.Retrofit

import com.example.project_b_security_gardapp.api.Services.UserServices
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val getInstance by lazy {
        Retrofit.Builder().baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}