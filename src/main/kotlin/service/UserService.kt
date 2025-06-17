package com.example.service

import com.example.model.ApiResponse
import com.example.model.FullUser
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

    fun getProfile(id: Int): ApiResponse<FullUser?> {
        return try {

            if (!userRepository.idExists(id)) {
                ApiResponse(
                    data = null,
                    success = false,
                    statusCode = HttpStatusCode.NotFound.value,
                    message = "This user doesnt exist"
                )
            } else {
                val user = userRepository.findUserById(id)
                ApiResponse(
                    data = user,
                    success = false,
                    statusCode = HttpStatusCode.OK.value,
                    message = "Profile Fetched Successfully"
                )
            }
        } catch (e: Exception) {
            ApiResponse(
                data = null,
                success = false,
                statusCode = HttpStatusCode.InternalServerError.value,
                message = "Error Occurred: ${e.message}"
            )
        }
    }

    fun searchUser(query: String): ApiResponse<List<FullUser>?> {
        return try {
            val user = userRepository.findUsersByQuery(query)
            ApiResponse(
                data = user,
                success = true,
                statusCode = HttpStatusCode.OK.value,
                message = "Profile Fetched Successfully"
            )

        } catch (e: Exception) {
            ApiResponse(
                data = null,
                success = false,
                statusCode = HttpStatusCode.InternalServerError.value,
                message = "Error Occurred: ${e.message}"
            )
        }
    }

    fun getProfileByUsername(name: String): ApiResponse<FullUser?> {
        return try {
            if (!userRepository.usernameExists(name)) {
                ApiResponse(
                    data = null,
                    success = false,
                    statusCode = HttpStatusCode.NotFound.value,
                    message = "This user doesnt exist"
                )
            } else {
                val user = userRepository.findUserByUsername(name)
                ApiResponse(
                    data = user,
                    success = false,
                    statusCode = HttpStatusCode.OK.value,
                    message = "Profile Fetched Successfully"
                )
            }
        } catch (e: Exception) {
            ApiResponse(
                data = null,
                success = false,
                statusCode = HttpStatusCode.InternalServerError.value,
                message = "Error Occurred: ${e.message}"
            )
        }
    }


}