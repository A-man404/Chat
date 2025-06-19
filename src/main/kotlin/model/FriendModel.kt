package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class BlockRequest(
    val userId: Int,
    val blockedUserId: Int
)

@Serializable
data class BlockList(
    val name: String,
    val userId: String
)
