package com.example

import com.example.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    DatabaseFactory().init()
    val jwtConfig = JWTConfig()

    configureSecurity(jwtConfig)

    configureSerialization()
    configureRouting(jwtConfig)
}
