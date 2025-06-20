package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class BlockRequest(
    val userId: Int,
    val blockedUserId: Int
)

@Serializable
data class BlockedUsersList(
    val id: Int,
    val name: String
)

@Serializable
data class FriendRequest(
    val senderId: Int,
    val receiverId: Int
)