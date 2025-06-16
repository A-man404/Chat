package com.example.service

import com.example.model.ApiResponse
import com.example.model.SignupRequest
import com.example.model.UserResponse
import com.example.repository.UserRepository
import io.ktor.http.*
import org.mindrot.jbcrypt.BCrypt

class AuthService {

    val userRepository: UserRepository = UserRepository()

    fun createUser(signupRequest: SignupRequest): ApiResponse<UserResponse?> {

        return try {
            if (userRepository.userExists(signupRequest.username, signupRequest.email, signupRequest.mobile)) {
                ApiResponse(
                    data = null,
                    success = false,
                    statusCode = HttpStatusCode.Conflict.value,
                    message = "User Already Exists with these credentials"
                )
            } else {
                val hashedPassword = BCrypt.hashpw(signupRequest.password, BCrypt.gensalt())
                val hashedUser = signupRequest.copy(password = hashedPassword)
                val createdUser = userRepository.createUser(hashedUser)
                ApiResponse(
                    data = UserResponse(
                        id = createdUser,
                        username = signupRequest.username,
                        email = signupRequest.email,
                        mobile = signupRequest.mobile,
                        profileImage = signupRequest.profileImage,
                        firstName = signupRequest.firstName,
                        lastName = signupRequest.lastName,
                        bio = signupRequest.bio
                    ),
                    success = true,
                    statusCode = HttpStatusCode.OK.value,
                    message = "User Created Successfully"
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