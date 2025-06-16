package com.example.repository

import com.example.model.FullUser
import com.example.model.SignupRequest
import com.example.model.UserResponse
import com.example.utils.toFullUser
import com.example.utils.toUserResponse
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import table.Users

class UserRepository {

    fun createUser(signupRequest: SignupRequest): Int {
        val user = transaction {
            Users.insert {
                it[username] = signupRequest.username
                it[email] = signupRequest.email
                it[mobile] = signupRequest.mobile
                it[hashedPassword] = signupRequest.password
                it[profilePhoto] = signupRequest.profileImage ?: "https://robohash.org/${signupRequest.firstName}"
                it[firstName] = signupRequest.firstName
                it[lastName] = signupRequest.lastName
                it[bio] = signupRequest.bio ?: ""
            } get Users.id
        }
        return user
    }

    fun userExists(username: String, email: String, mobile: String): Boolean {
        return transaction {
            Users.selectAll().where {
                (Users.username eq username) or (Users.email eq email) or (Users.mobile eq mobile)
            }.limit(1).any()
        }
    }

    fun emailExists(email: String): Boolean {
        return transaction {
            Users.selectAll().where {
                (Users.email eq email)
            }.limit(1).any()
        }
    }

    fun listAllUsers(): List<UserResponse> {
        return transaction {
            Users.selectAll().map { it.toUserResponse() }
        }

    }

    fun findUser(email: String): FullUser? {
        return transaction {
            Users.select(Users.email eq email).map {
                it.toFullUser()
            }.singleOrNull()
        }
    }
}
