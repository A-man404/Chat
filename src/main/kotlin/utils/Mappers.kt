package com.example.utils

import com.example.model.FullUser
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
    bio = this[Users.bio]
)

fun ResultRow.toFullUser(): FullUser {
    return FullUser(
        id = this[Users.id],
        username = this[Users.username],
        email = this[Users.email],
        mobile = this[Users.mobile],
        hashedPassword = this[Users.hashedPassword],
        firstName = this[Users.firstName],
        lastName = this[Users.lastName],
        profileImage = this[Users.profilePhoto],
        bio = this[Users.bio],
        isVerified = this[Users.isVerified],
        role = this[Users.role],
        createdAt = this[Users.createdAt].toString(),
        updatedAt = this[Users.updatedAt].toString(),
        accountStatus = this[Users.accountStatus]
    )
}
