package com.example.plugins

import com.example.routes.authRoutes
import com.example.routes.friendRequestRoutes
import com.example.routes.friendRoutes
import com.example.routes.userRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(jwtConfig: JWTConfig) {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        authRoutes(jwtConfig)
        userRoutes()
        friendRoutes()
        friendRequestRoutes()
    }
}
