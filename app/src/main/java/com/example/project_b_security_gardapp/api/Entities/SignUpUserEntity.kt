package com.example.project_b_security_gardapp.api.Entities

data class SignUpUserEntity(
    val fullName: String,
    val password: String,
    val phoneNumber: String,
    val societyCode: String
)