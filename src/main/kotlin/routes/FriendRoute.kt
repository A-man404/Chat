package com.example.routes

import com.example.model.BlockRequest
import com.example.plugins.UserPrincipal
import com.example.service.BlockService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.friendRoutes() {

    val blockService = BlockService()
    authenticate("jwt-auth") {

        post("/block") {
            try {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.InternalServerError,
                    "Token Invalid or missing"
                )
                val blockedUserId = call.queryParameters["blockId"] ?: return@post call.respond(
                    HttpStatusCode.InternalServerError,
                    "Input a block request"
                )
                val result =
                    blockService.blockUser(
                        BlockRequest(
                            userId = principal.userId,
                            blockedUserId = blockedUserId.toInt()
                        )
                    )

                call.respond(HttpStatusCode.fromValue(result.statusCode), result)

            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error Occurred: ${e.message}")

            }
        }
        post("/unblock") {
            try {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.InternalServerError,
                    "Token Invalid or missing"
                )
                val blockedUserId = call.queryParameters["blockId"] ?: return@post call.respond(
                    HttpStatusCode.InternalServerError,
                    "Input a block request"
                )
                val result =
                    blockService.unBlockUser(
                        BlockRequest(
                            userId = principal.userId,
                            blockedUserId = blockedUserId.toInt()
                        )
                    )

                call.respond(HttpStatusCode.fromValue(result.statusCode), result)

            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error Occurred: ${e.message}")

            }
        }
    }
}