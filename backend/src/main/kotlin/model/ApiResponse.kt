package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val data: T?,
    val message: String,
    val success: Boolean,
    val statusCode: Int
)
