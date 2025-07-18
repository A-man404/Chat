package com.example.routes

import com.example.model.ForgotPassword
import com.example.model.LoginRequest
import com.example.model.SignupRequest
import com.example.plugins.JWTConfig
import com.example.plugins.UserPrincipal
import com.example.service.AuthService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.authRoutes(jwtConfig: JWTConfig) {

    val authService = AuthService()
    val storeOTP = mutableMapOf<Int, String>()



    route("/auth") {

        post("/signup") {
            try {
                val request = call.receive<SignupRequest>()
                val result = authService.createUser(request)
                call.respond(HttpStatusCode.fromValue(result.statusCode), result)
            } catch (e: Exception) {
                call.respond(message = e.message.toString(), status = HttpStatusCode.InternalServerError)
            }
        }

        post("/login") {
            try {
                val request = call.receive<LoginRequest>()
                val result = authService.loginUser(request, jwtConfig)
                call.respond(HttpStatusCode.fromValue(result.statusCode), result)
            } catch (e: Exception) {
                call.respond(message = e.message.toString(), status = HttpStatusCode.InternalServerError)
            }
        }
    }

    authenticate("jwt-auth") {

        post("sendForgotOtp") {
            try {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.BadRequest,
                    "Token invalid or missing"
                )
                val email = call.queryParameters["email"] ?: return@post call.respond(
                    HttpStatusCode.BadRequest,
                    "Please enter an email"
                )
                val result = authService.forgotPasswordOtp(email)
                if (result.success) {
                    storeOTP[principal.userId] = result.data ?: ""
                    call.respond(message = result, status = HttpStatusCode.fromValue(result.statusCode))
                } else {
                    call.respond(message = "Error", status = HttpStatusCode.InternalServerError)
                }
            } catch (e: Exception) {
                call.respond(message = e.message.toString(), status = HttpStatusCode.InternalServerError)

            }
        }

        post("verifyForgotOtp") {
            try {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.BadRequest, "Token invalid or missing"
                )
                val rec = call.receive<ForgotPassword>()


                val otpStored = storeOTP[principal.userId]

                if (otpStored == null) {
                    return@post call.respond(HttpStatusCode.BadRequest, "OTP expired or not requested")
                }

                val result = authService.forgotPasswordVerify(principal.userId, rec.otp, otpStored)

                if (!result.success) {
                    return@post call.respond(
                        message = result.message,
                        status = HttpStatusCode.fromValue(result.statusCode)
                    )
                }

                val change = authService.forgotPasswordChange(principal.userId, rec.newPassword)
                if (change.success) {
                    call.respond(HttpStatusCode.fromValue(change.statusCode), change)
                } else {
                    call.respond(message = "Error Occurred", status = HttpStatusCode.InternalServerError)
                }
            } catch (e: Exception) {
                call.respond(message = e.message ?: "Unknown error", status = HttpStatusCode.InternalServerError)
            }
        }

    }


}