package com.example.service

import com.example.model.ApiResponse
import com.example.model.BlockRequest
import com.example.repository.BlockRepository
import com.example.repository.UserRepository
import io.ktor.http.*

class BlockService {

    val blockRepository = BlockRepository()
    val userRepository = UserRepository()

    fun blockUser(blockRequest: BlockRequest): ApiResponse<String?> {
        return if (!userRepository.idExists(blockRequest.userId) && !userRepository.idExists(blockRequest.blockedUserId)) {
            ApiResponse(null, "Users doesn't exist", false, HttpStatusCode.NotFound.value)
        } else if (blockRequest.userId == blockRequest.blockedUserId) {
            ApiResponse(null, "You Cannot Block Yourself", false, HttpStatusCode.NotFound.value)

        } else if (blockRepository.userAlreadyBlocked(blockRequest)) {
            ApiResponse(null, "User Already Blocked", false, HttpStatusCode.Conflict.value)
        } else {
            val block = blockRepository.blockUser(blockRequest)

            //TODO: remove from friend list as well

            ApiResponse(block.toString(), "User Already Blocked", true, HttpStatusCode.OK.value)
        }

    }

    fun unBlockUser(blockRequest: BlockRequest): ApiResponse<String?> {

        return if (!userRepository.idExists(blockRequest.userId) && !userRepository.idExists(blockRequest.blockedUserId)) {
            println(blockRequest.blockedUserId)
            ApiResponse(null, "Users doesn't exist", false, HttpStatusCode.NotFound.value)
        } else if (blockRepository.userAlreadyBlocked(blockRequest)) {
            val block = blockRepository.unblockUser(blockRequest)

            ApiResponse(block.toString(), "User Unblocked Successfully", true, HttpStatusCode.OK.value)

        } else {
            ApiResponse(null, "Error", false, HttpStatusCode.InternalServerError.value)
        }

    }

    fun listBlockedUsers(id: Int): ApiResponse<String?> {

        return if (!userRepository.idExists(id)) {
            ApiResponse(null, "Users doesn't exist", false, HttpStatusCode.NotFound.value)
        } else if (false) {
            //TODO: call the list all blocked users from repository
            ApiResponse(null, "Users doesn't exist", false, HttpStatusCode.NotFound.value)

        } else {
            ApiResponse(null, "Error", false, HttpStatusCode.InternalServerError.value)
        }

    }


}