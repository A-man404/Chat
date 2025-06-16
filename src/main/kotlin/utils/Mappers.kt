package com.example.utils

import com.example.model.UserResponse
import org.jetbrains.exposed.sql.ResultRow
import table.User


fun ResultRow.toUserResponse(): UserResponse = UserResponse(
    id = this[User.id],
    username = this[User.username],
    email = this[User.email],
    mobile = this[User.mobile],
    profileImage = this[User.profilePhoto],
    firstName = this[User.firstName],
    lastName = this[User.lastName],
    bio = this[User.bio]
)