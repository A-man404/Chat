package com.example.utils

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