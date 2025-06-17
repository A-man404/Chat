package com.example.model

import kotlinx.serialization.Serializable
import table.AccountStatus
import table.ROLE

@Serializable
data class SignupRequest(
    val username: String,
    val email: String,
    val password: String,
    val mobile: String,
    val profileImage: String? = null,
    val firstName: String,
    val lastName: String? = null,
    val bio: String? = null,
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
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

@Serializable
data class UserProfile(
    val username: String,
    val profileImage: String? = null,
    val firstName: String,
    val lastName: String? = null,
    val bio: String?,
)

@Serializable
data class FullUser(
    val id: Int,
    val username: String,
    val email: String,
    val mobile: String,
    val hashedPassword: String,
    val firstName: String,
    val lastName: String?,
    val profileImage: String,
    val bio: String,
    val isVerified: Boolean,
    val role: ROLE,
    val createdAt: String,
    val updatedAt: String,
    val accountStatus: AccountStatus
)
