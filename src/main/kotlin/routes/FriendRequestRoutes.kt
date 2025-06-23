package com.example.routes

import com.example.model.FriendRequest
import com.example.plugins.UserPrincipal
import com.example.service.FriendRequestService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.friendRequestRoutes() {

    val friendRequestService = FriendRequestService()
    authenticate("jwt-auth") {
        route("/friend-requests") {
            get("list") {
                val principal = call.principal<UserPrincipal>() ?: return@get call.respond(
                    HttpStatusCode.Unauthorized,
                    "Token Invalid or not found"
                )
                val res = friendRequestService.listFriendRequest(principal.userId)
                call.respond(res)
            }
            post("/add") {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.Unauthorized,
                    "Token Invalid or not found"
                )
                val id = call.queryParameters["id"] ?: return@post call.respond(
                    HttpStatusCode.BadRequest,
                    "Id must be specified"
                )
                if(principal.userId == id.toInt()) {
                    return@post call.respond(HttpStatusCode.BadRequest, "Both can't be same")
                }
                val res = friendRequestService.addFriendRequest(
                    FriendRequest(
                        senderId = principal.userId,
                        receiverId = id.toInt()
                    )
                )
                call.respond(res)
            }
            post("/remove") {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.Unauthorized,
                    "Token Invalid or not found"
                )

                val id = call.queryParameters["id"] ?: return@post call.respond(
                    HttpStatusCode.BadRequest,
                    "Id must be specified"
                )
                if(principal.userId == id.toInt()) {
                    return@post call.respond(HttpStatusCode.BadRequest, "Both can't be same")
                }
                val res = friendRequestService.removeFriendRequest(
                    FriendRequest(
                        senderId = principal.userId,
                        receiverId = id.toInt()
                    )
                )
                call.respond(res)
            }

        }

    }
}