package com.example.service

import com.example.model.AddFriend
import com.example.model.ApiResponse
import com.example.model.FriendList
import com.example.repository.FriendRepository
import io.ktor.http.*

class FriendService {

    val friendRepository = FriendRepository()

    fun addFriend(addFriend: AddFriend): ApiResponse<Int> {
        return try {
            if (addFriend.userId == addFriend.friendId) {
                ApiResponse(
                    null, "You cannot add yourself as a friend", false, HttpStatusCode.BadRequest.value
                )
            } else if (friendRepository.alreadyFriend(addFriend)) {
                ApiResponse(
                    null, "Friend Already Added", false, HttpStatusCode.BadRequest.value
                )
            } else {
                val res = friendRepository.addFriend(addFriend)
                ApiResponse(
                    res, "Friend Added Successfully", false, HttpStatusCode.OK.value
                )
            }
        } catch (e: Exception) {
            ApiResponse(
                null, "Error Occurred : ${e.message}", false, HttpStatusCode.InternalServerError.value
            )
        }
    }

    fun removeFriend(addFriend: AddFriend): ApiResponse<Int> {

        return try {
            if (addFriend.userId == addFriend.friendId) {
                ApiResponse(
                    null, "You cannot remove yourself as a friend", false, HttpStatusCode.BadRequest.value
                )
            } else if (friendRepository.alreadyFriend(addFriend)) {
                val res = friendRepository.removeFriend(addFriend)
                ApiResponse(
                    res, "Friend Removed Successfully", false, HttpStatusCode.OK.value
                )
            } else {
                ApiResponse(
                    null, "Error Occurred", false, HttpStatusCode.BadRequest.value
                )
            }
        } catch (e: Exception) {
            ApiResponse(
                null, "Error Occurred : ${e.message}", false, HttpStatusCode.InternalServerError.value
            )
        }

    }

    fun friendsList(id: Int): ApiResponse<List<FriendList>> {
        return try {
            val res = friendRepository.fetchFriends(id)
            ApiResponse(
                res, "Friend List Fetched Successfully", true, HttpStatusCode.OK.value
            )
        } catch (e: Exception) {
            ApiResponse(
                null, "Error Occurred : ${e.message}", false, HttpStatusCode.InternalServerError.value
            )
        }

    }


}