package com.example.project_b_security_gardapp.api.Entities

import java.io.File

data class RequestsResultEntity(
    val checkInTime: String?,     // null possible
    val checkOutTime: String?,    // null possible
    val createdAt: String,
    val description: String,
    val flatNumber: String,
    val guardId: Int,
    val guardName: String,
    val guestName: String,
    val id: Int,
    val ownerId: Int,
    val ownerName: String,
    val phoneNumber: String,
    val photo1: String?,          // null possible
    val photo2: String?,          // null possible
    val societyName: String,
    val status: String,
    val updatedAt: String,
    val visitDate: String?        // null possible

)