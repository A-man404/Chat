package com.example.routes

import com.example.model.SignupRequest
import com.example.service.AuthService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.authRoutes() {

    val authService = AuthService()

    route("/user") {

        post("/create") {
            try {
                val request = call.receive<SignupRequest>()
                val result = authService.createUser(request)
                call.respond(HttpStatusCode.fromValue(result.statusCode), result)
            } catch (e: Exception) {
                call.respond(message = e.message.toString(), status = HttpStatusCode.InternalServerError)
            }
        }

    }


}