package com.example.service

import com.example.model.ApiResponse
import com.example.model.BlockRequest
import com.example.model.FriendRequest
import com.example.model.FriendRequestDTO
import com.example.repository.BlockRepository
import com.example.repository.FriendRequestRepository
import io.ktor.http.*

class FriendRequestService {

    val blockRepository = BlockRepository()
    val friendRequestRepo = FriendRequestRepository();


    fun addFriendRequest(friendRequest: FriendRequest): ApiResponse<String?> {
        return if (blockRepository.userAlreadyBlocked(
                BlockRequest(
                    userId = friendRequest.senderId,
                    blockedUserId = friendRequest.receiverId,
                )
            )
        ) {
            //TODO: add a conditional where user is already friends with them
            ApiResponse(
                data = "User is blocked",
                message = "User is blocked",
                success = false,
                statusCode = HttpStatusCode.Unauthorized.value
            )
        } else if (friendRequestRepo.checkFriendRequest(friendRequest)) {
            ApiResponse(
                data = "You have already sent them friend request",
                message = "You have already sent them friend request",
                success = false,
                statusCode = HttpStatusCode.Unauthorized.value
            )
        } else {

            val res = friendRequestRepo.addFriendRequest(friendRequest)
            ApiResponse(
                data = res.toString(),
                message = "Friend Request Sent Successfully",
                success = true,
                statusCode = HttpStatusCode.OK.value
            )
        }
    }

    fun removeFriendRequest(friendRequest: FriendRequest): ApiResponse<String?> {
        return if (blockRepository.userAlreadyBlocked(
                BlockRequest(
                    userId = friendRequest.senderId,
                    blockedUserId = friendRequest.receiverId,
                )
            )
        ) {
            //TODO: add a conditional where user is already friends with them
            ApiResponse(
                data = "User is blocked",
                message = "User is blocked",
                success = false,
                statusCode = HttpStatusCode.Unauthorized.value
            )
        } else if (!friendRequestRepo.checkFriendRequest(friendRequest)) {
            ApiResponse(
                data = "You havent send them friend request",
                message = "You havent send them friend request",
                success = false,
                statusCode = HttpStatusCode.Unauthorized.value
            )
        } else {
            val res = friendRequestRepo.removeFriendRequest(friendRequest)
            ApiResponse(
                data = res.toString(),
                message = "Friend Request removed Successfully",
                success = true,
                statusCode = HttpStatusCode.OK.value
            )
        }
    }

    fun listFriendRequest(id: Int): ApiResponse<List<FriendRequestDTO>?> {

        val res = friendRequestRepo.getFriendRequestsList(id)
        return ApiResponse(
            data = res,
            message = "Friend list fetched successfully",
            success = true,
            statusCode = HttpStatusCode.OK.value
        )

    }

}