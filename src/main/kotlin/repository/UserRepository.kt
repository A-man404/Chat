package com.example.repository

import com.example.model.SignupRequest
import com.example.model.UserResponse
import com.example.utils.toUserResponse
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import table.User

class UserRepository {

    fun createUser(signupRequest: SignupRequest): Int {
        val user = transaction {
            User.insert {
                it[username] = signupRequest.username
                it[email] = signupRequest.email
                it[mobile] = signupRequest.mobile
                it[hashedPassword] = signupRequest.password
                it[profilePhoto] = signupRequest.profileImage ?: "https://robohash.org/${signupRequest.firstName}"
                it[firstName] = signupRequest.firstName
                it[lastName] = signupRequest.lastName
                it[bio] = signupRequest.bio ?: ""
            } get User.id
        }
        return user
    }

    fun userExists(username: String, email: String, mobile: String): Boolean {
        return transaction {
            User.selectAll().where {
                (User.username eq username) or
                        (User.email eq email) or
                        (User.mobile eq mobile)
            }.limit(1).any()
        }
    }

    fun listAllUsers(): List<UserResponse> {
        return transaction {
            User.selectAll().map { it.toUserResponse() }
        }

    }
}
