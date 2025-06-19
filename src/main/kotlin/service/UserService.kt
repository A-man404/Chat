package com.example.service

import com.example.model.ApiResponse
import com.example.model.FullUser
import com.example.model.UserResponse
import com.example.plugins.emailSender
import com.example.repository.UserRepository
import io.ktor.http.*
import kotlin.random.Random

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

            if (!user.isEmpty()) {
                ApiResponse(
                    data = user,
                    success = true,
                    statusCode = HttpStatusCode.OK.value,
                    message = "Profile Fetched Successfully"
                )
            } else {
                ApiResponse(
                    data = null,
                    success = false,
                    statusCode = HttpStatusCode.NotFound.value,
                    message = "No User Found"
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

    fun getProfileByUsername(name: String): ApiResponse<FullUser?> {
        return try {

            if (!userRepository.usernameExists(name.trim().lowercase())) {
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
                    success = true,
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

    fun sendVerificationOtp(id: Int): ApiResponse<String> {
        return try {
            val random = Random.nextInt(100000, 999999)

            val user = userRepository.findUserById(id) ?: return ApiResponse(
                data = null,
                success = false,
                statusCode = HttpStatusCode.InternalServerError.value,
                message = "Error Occurred"
            )


            emailSender(
                subject = "Email Verification",
                sendTo = user.email,
                message = "Hi ${user.firstName},\n" +
                        "\n" +
                        "Your one-time password (OTP) is ${random}.\n" +
                        "\n" +
                        "Please do not share this code with anyone.\n" +
                        "\n" +
                        "If you didn’t request this code, you can safely ignore this email or contact our support team.\n" +
                        "\n" +
                        "Thank you,\n" +
                        "Void Chat Team"
            )
            ApiResponse(
                data = random.toString(),
                success = true,
                statusCode = HttpStatusCode.OK.value,
                message = "Email Sent Successfully"
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

    fun verifyEmailOtp(id: Int, sentOtp: String, storeOtp: String): ApiResponse<String> {
        return try {

            if (!userRepository.idExists(id)) {
                ApiResponse(
                    data = "User Doesnt Exist",
                    success = false,
                    statusCode = HttpStatusCode.BadRequest.value,
                    message = "User Doesnt exist"
                )
            } else {
                if (sentOtp == storeOtp) {
                    userRepository.changeUserVerification(id)
                    ApiResponse(
                        data = "Email Verified Successfully",
                        success = true,
                        statusCode = HttpStatusCode.OK.value,
                        message = "Email Verified Successfully"
                    )
                } else {
                    ApiResponse(
                        data = "Incorrect Otp",
                        success = false,
                        statusCode = HttpStatusCode.OK.value,
                        message = "Email Unverified"
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