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

@Serializable
data class FriendRequestDTO(
    val type: String,
    val otherId: Int,
    val name: String,
    val profileImage: String?
)

@Serializable
data class AddFriend(
    val userId: Int,
    val friendId: Int
)

@Serializable
data class FriendList(
    val friendId: Int,
    val chatId: Int,
    val username: String?,
    val profileImage: String?
)
