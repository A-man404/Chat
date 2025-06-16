package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    val username: String,
    val email: String,
    val password: String,
    val mobile: String,
    val profileImage: String? = null,
    val firstName: String,
    val lastName: String? = null,
    val bio: String?=null,
)


@Serializable
data class UserResponse(
    val id: Int,
    val username: String,
    val email: String,
    val mobile: String,
    val profileImage: String? = null,
    val firstName: String,
    val lastName: String? = null,
    val bio: String?,
)