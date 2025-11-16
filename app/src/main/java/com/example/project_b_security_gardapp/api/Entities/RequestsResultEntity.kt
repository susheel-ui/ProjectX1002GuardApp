package com.example.project_b_security_gardapp.api.Entities

data class RequestsResultEntity(
    val checkInTime: Any,
    val checkOutTime: Any,
    val createdAt: List<Int>,
    val description: String,
    val flatNumber: String,
    val guardId: Int,
    val guardName: String,
    val guestName: String,
    val id: Int,
    val ownerId: Int,
    val ownerName: String,
    val phoneNumber: String,
    val photo1: String,
    val photo2: String,
    val societyName: String,
    val status: String,
    val updatedAt: List<Int>,
    val visitDate: Any
)