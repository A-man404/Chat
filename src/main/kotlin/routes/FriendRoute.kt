package com.example.routesInt

import com.example.model.AddFriend
import com.example.model.BlockRequest
import com.example.plugins.UserPrincipal
import com.example.service.BlockService
import com.example.service.FriendService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.friendRoutes() {

    val blockService = BlockService()
    val friendService = FriendService()
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
        get("/blocklist") {
            try {
                val principal = call.principal<UserPrincipal>() ?: return@get call.respond(
                    HttpStatusCode.InternalServerError,
                    "Token Invalid or missing"
                )
                val result =
                    blockService.listBlockedUsers(principal.userId)

                call.respond(HttpStatusCode.fromValue(result.statusCode), result)

            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error Occurred: ${e.message}")

            }
        }


        route("/friend") {
            post("/add") {
                try {
                    val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                        HttpStatusCode.InternalServerError,
                        "Token Invalid or missing"
                    )
                    val friendId = call.queryParameters["friendId"] ?: return@post call.respond(
                        HttpStatusCode.InternalServerError,
                        "Input a friend"
                    )
                    val res = friendService.addFriend(AddFriend(principal.userId, friendId.toInt()))
                    call.respond(HttpStatusCode.OK, res)

                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, e.message ?: "Bad Request")
                }
            }
            post("/remove") {
                try {
                    val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                        HttpStatusCode.InternalServerError,
                        "Token Invalid or missing"
                    )
                    val friendId = call.queryParameters["friendId"] ?: return@post call.respond(
                        HttpStatusCode.InternalServerError,
                        "Input a friend"
                    )
                    val res = friendService.removeFriend(AddFriend(principal.userId, friendId.toInt()))
                    call.respond(HttpStatusCode.OK, res)

                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, e.message ?: "Bad Request")
                }
            }

        }


    }
}