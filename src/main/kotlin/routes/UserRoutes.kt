package com.example.routes

import com.example.service.UserService
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.userRoutes() {
    val userService = UserService()
    route("/users") {
        get("/all") {
            try {
                val users = userService.listAllUsers()
                call.respond(HttpStatusCode.fromValue(users.statusCode), users)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error Occurred: ${e.message}")
            }

        }
    }
}