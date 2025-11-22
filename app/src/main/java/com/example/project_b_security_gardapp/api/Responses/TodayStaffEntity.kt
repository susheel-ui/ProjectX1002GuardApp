package com.example.project_b_security_gardapp.api.Responses

data class TodayStaffEntity(
    val checkInTime: Any,
    val checkOutTime: Any,
    val createdAt: String,
    val id: Int,
    val societyName: String,
    val staffCode: Int,
    val staffId: Int,
    val staffName: String
)