package com.example.service

import com.example.model.ApiResponse
import com.example.model.LoginRequest
import com.example.model.SignupRequest
import com.example.model.UserResponse
import com.example.plugins.JWTConfig
import com.example.plugins.emailSender
import com.example.plugins.generateToken
import com.example.repository.UserRepository
import com.example.utils.PasswordHasher
import io.ktor.http.*
import kotlin.random.Random

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
                        bio = signupRequest.bio,

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

    fun loginUser(loginRequest: LoginRequest, jwtConfig: JWTConfig): ApiResponse<String?> {
        return try {
            if (!userRepository.emailExists(loginRequest.email)) {
                ApiResponse(
                    data = null,
                    success = false,
                    statusCode = HttpStatusCode.NotFound.value,
                    message = "This user doesnt exist"
                )
            } else {
                val user = userRepository.findUserByEmail(loginRequest.email)
                if (!PasswordHasher.verifyPassword(loginRequest.password, user?.hashedPassword ?: "")) {
                    ApiResponse(
                        data = null,
                        success = false,
                        statusCode = HttpStatusCode.InternalServerError.value,
                        message = "Your Password is incorrect"
                    )
                } else {
                    val token = generateToken(jwtConfig, user?.id, user?.role)
                    ApiResponse(
                        data = token,
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

    fun changePassword(id: Int, oldPassword: String, newPassword: String): ApiResponse<String?> {
        return try {
            if (!userRepository.idExists(id)) {
                ApiResponse(
                    data = null,
                    success = false,
                    statusCode = HttpStatusCode.NotFound.value,
                    message = "This user doesn't exist"
                )
            } else {
                val user = userRepository.findUserById(id) ?: return ApiResponse(
                    data = null,
                    success = false,
                    statusCode = HttpStatusCode.NotFound.value,
                    message = "This user doesn't exist"
                )
                val isVerified = PasswordHasher.verifyPassword(oldPassword, user.hashedPassword)

                if (isVerified) {
                    userRepository.changePassword(user.id, newPassword)
                    ApiResponse(
                        data = "Password Changed Successfully",
                        success = true,
                        statusCode = HttpStatusCode.OK.value,
                        message = "Done"
                    )
                } else {
                    ApiResponse(
                        data = null,
                        success = false,
                        statusCode = HttpStatusCode.Unauthorized.value,
                        message = "Your password is incorrect"
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

    fun forgotPasswordOtp(email: String): ApiResponse<String> {
        return try {
            val random = Random.nextInt(100000, 999999)

            val user = userRepository.findUserByEmail(email) ?: return ApiResponse(
                data = null,
                success = false,
                statusCode = HttpStatusCode.InternalServerError.value,
                message = "Error Occurred"
            )


            emailSender(
                subject = "Change Password",
                sendTo = user.email,
                message = "Hi ${user.firstName},\n" +
                        "\n" +
                        "Your one-time password (OTP) for change password is ${random}.\n" +
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

    fun forgotPasswordVerify(id: Int, sentOtp: String, storeOtp: String): ApiResponse<String> {
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

    fun forgotPasswordChange(id: Int, newPassword: String): ApiResponse<String> {
        return try {
            val user = userRepository.findUserById(id) ?: return ApiResponse(
                data = null,
                success = false,
                statusCode = HttpStatusCode.InternalServerError.value,
                message = "Error Occurred"
            )
            userRepository.changePassword(user.id, newPassword)
            ApiResponse(
                data = "Password Changed Successfully",
                success = true,
                statusCode = HttpStatusCode.OK.value,
                message = "Changed Password Successfully"
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


}