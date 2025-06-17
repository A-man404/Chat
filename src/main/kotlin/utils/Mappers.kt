package com.example.utils

import com.example.model.FullUser
import com.example.model.UserProfile
import com.example.model.UserResponse
import org.jetbrains.exposed.sql.ResultRow
import table.Users


fun ResultRow.toUserResponse(): UserResponse = UserResponse(
    id = this[Users.id],
    username = this[Users.username],
    email = this[Users.email],
    mobile = this[Users.mobile],
    profileImage = this[Users.profilePhoto],
    firstName = this[Users.firstName],
    lastName = this[Users.lastName],
    bio = this[Users.bio],
)

fun ResultRow.toFullUser(): FullUser {
    return FullUser(
        id = this[Users.id],
        username = this[Users.username],
        email = this[Users.email],
        mobile = this[Users.mobile],
        firstName = this[Users.firstName],
        lastName = this[Users.lastName],
        profileImage = this[Users.profilePhoto],
        hashedPassword = this[Users.hashedPassword],
        role = this[Users.role],
        bio = this[Users.bio],
        isVerified = this[Users.isVerified],
        createdAt = this[Users.createdAt].toString(),
        updatedAt = this[Users.updatedAt].toString(),
        accountStatus = this[Users.accountStatus]
    )
}

fun FullUser.toUserProfile(): UserProfile =
    UserProfile(
        username = this.username,
        profileImage = this.profileImage,
        firstName = this.firstName,
        lastName = this.lastName,
        bio = this.bio
    )

