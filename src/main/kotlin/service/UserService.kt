package com.example.service

import com.example.model.ApiResponse
import com.example.model.UserResponse
import com.example.repository.UserRepository
import io.ktor.http.*

class UserService {
    val userRepository = UserRepository()


    fun listAllUsers(): ApiResponse<List<UserResponse>> {
        return try {
            val users = userRepository.listAllUsers()
            ApiResponse(
                data = users,
                message = "User Fetched Successfully",
                success = true,
                statusCode = HttpStatusCode.OK.value
            )

        } catch (e: Exception) {
            ApiResponse(
                data = null,
                message = "Error Occurred ${e.message}",
                success = false,
                statusCode = HttpStatusCode.InternalServerError.value
            )
        }
    }

}