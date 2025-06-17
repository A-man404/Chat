package com.example.routes

import com.example.model.LoginRequest
import com.example.model.SignupRequest
import com.example.plugins.JWTConfig
import com.example.service.AuthService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.authRoutes(jwtConfig: JWTConfig) {

    val authService = AuthService()


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


}