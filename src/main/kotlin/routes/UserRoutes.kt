package com.example.routes

import com.example.model.ChangePassword
import com.example.plugins.UserPrincipal
import com.example.service.AuthService
import com.example.service.UserService
import com.example.utils.toUserProfile
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.userRoutes() {

    val userService = UserService()
    val authService = AuthService()

    authenticate("jwt-auth") {

        val storeOTP = mutableMapOf<Int, String>()

        get("/sendOTP") {

            val principal = call.principal<UserPrincipal>() ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                "Token invalid or missing"
            )
            val id = principal.userId
            val res = userService.sendVerificationOtp(id)
            storeOTP[id] = res.data ?: ""
            call.respond(HttpStatusCode.fromValue(res.statusCode), res)
        }

        post("/verifyUser") {
            val otp = call.queryParameters["otp"]
            val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                HttpStatusCode.BadRequest,
                "Token invalid or missing"
            )
            val id = principal.userId
            val res = userService.verifyEmailOtp(id, otp.toString(), storeOTP[id].toString())
            if (res.success) {
                storeOTP.remove(id)
            }
            call.respond(res)
        }

        route("/user") {

            get("me") {
                try {
                    val principal = call.principal<UserPrincipal>() ?: return@get call.respond(
                        HttpStatusCode.BadRequest,
                        "Token invalid or missing"
                    )
                    val id = principal.userId
                    val res = userService.getProfile(id)
                    call.respond(HttpStatusCode.fromValue(res.statusCode), res)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Error Occurred: ${e.message}")
                }
            }

            get("/profile") {
                try {
                    call.principal<UserPrincipal>() ?: return@get call.respond(
                        HttpStatusCode.BadRequest,
                        "Token invalid or missing"
                    )
                    val username = call.request.queryParameters["username"] ?: return@get call.respond(
                        HttpStatusCode.BadRequest,
                        "Bad Request"
                    )
                    val res = userService.getProfileByUsername(username)
                    val user = res.data?.toUserProfile() ?: return@get call.respond(
                        HttpStatusCode.BadRequest,
                        "Please send a username"
                    )
                    call.respond(HttpStatusCode.fromValue(res.statusCode), user)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Error Occurred: ${e.message}")
                }
            }

            get("/search") {
                try {
                    call.principal<UserPrincipal>() ?: return@get call.respond(
                        HttpStatusCode.BadRequest,
                        "Token invalid or missing"
                    )
                    val query = call.request.queryParameters["q"] ?: return@get call.respond(
                        HttpStatusCode.BadRequest,
                        "Bad Request"
                    )
                    val res = userService.searchUser(query)
                    val users = res.data?.map { it.toUserProfile() } ?: return@get call.respond(
                        HttpStatusCode.BadRequest,
                        "Bad Request"
                    )
                    call.respond(HttpStatusCode.fromValue(res.statusCode), users)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Error Occurred: ${e.message}")
                }
            }

            post("/changePassword") {
                try {
                    val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                        HttpStatusCode.BadRequest,
                        "Token invalid or missing"
                    )
                    val req = call.receive<ChangePassword>()
                    val res = authService.changePassword(
                        principal.userId,
                        oldPassword = req.oldPassword,
                        newPassword = req.newPassword
                    )
                    call.respond(HttpStatusCode.fromValue(res.statusCode), res)

                } catch (e: Exception) {
                    call.respond(message = e.message.toString(), status = HttpStatusCode.InternalServerError)
                }
            }

        }
    }
}