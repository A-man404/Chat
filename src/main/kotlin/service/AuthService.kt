package com.example.service

import com.example.model.ApiResponse
import com.example.model.LoginRequest
import com.example.model.SignupRequest
import com.example.model.UserResponse
import com.example.plugins.JWTConfig
import com.example.plugins.generateToken
import com.example.repository.UserRepository
import com.example.utils.PasswordHasher
import io.ktor.http.*

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
                val hashedPassword = PasswordHasher.hashPassword(signupRequest.password)
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

    fun loginUser(loginRequest: LoginRequest, jwtConfig: JWTConfig): ApiResponse<UserResponse?> {
        return try {
            if (!userRepository.emailExists(loginRequest.email)) {
                ApiResponse(
                    data = null,
                    success = false,
                    statusCode = HttpStatusCode.NotFound.value,
                    message = "This user doesnt exist"
                )
            } else {
                val user = userRepository.findUser(loginRequest.email)
                if (!PasswordHasher.verifyPassword(loginRequest.password, user?.hashedPassword ?: "")) {
                    ApiResponse(
                        data = null,
                        success = false,
                        statusCode = HttpStatusCode.InternalServerError.value,
                        message = "Your Password is incorrect"
                    )
                } else {
                    val token = generateToken(jwtConfig, user?.id ?: "", user?.role ?: )
                    ApiResponse(
                        data = null,
                        success = true,
                        statusCode = HttpStatusCode.OK.value,
                        message = "User Logged In Successfully"
                    )
                }
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