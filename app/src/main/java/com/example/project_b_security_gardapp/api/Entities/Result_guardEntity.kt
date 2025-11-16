package com.example.project_b_security_gardapp.api.Entities

data class Result_guardEntity(
    val createdAt: String,
    val enabled: Boolean,
    val flatNumber: String,
    val fullName: String,
    val id: Int,
    val password: String,
    val phoneNumber: String,
    val role: String,
    val societyCode: String,
    val societyName: String,
    val updatedAt: String
)