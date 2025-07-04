package com.example.repository

import com.example.model.FullUser
import com.example.model.SignupRequest
import com.example.model.UserResponse
import com.example.utils.PasswordHasher
import com.example.utils.toFullUser
import com.example.utils.toUserResponse
import org.jetbrains.exposed.sql.*
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

    fun usernameExists(username: String): Boolean {
        return transaction {
            Users.selectAll().where {
                (Users.username.trim().lowerCase() eq username.trim().lowercase())
            }.limit(1).any()
        }
    }

    fun idExists(id: Int): Boolean {
        return transaction {
            Users.selectAll().where {
                (Users.id eq id)
            }.limit(1).any()
        }
    }

    fun listAllUsers(): List<UserResponse> {
        return transaction {
            Users.selectAll().map { it.toUserResponse() }
        }

    }

    fun findUserByEmail(email: String): FullUser? {
        return transaction {
            Users
                .selectAll()
                .where { Users.email eq email }
                .map { it.toFullUser() }
                .singleOrNull()
        }
    }

    fun findUserByUsername(username: String): FullUser? {
        return transaction {
            Users
                .selectAll()
                .where {
                    Users.username.trim().lowerCase() eq username.trim().lowercase()
                }
                .map { it.toFullUser() }
                .singleOrNull()
        }
    }

    fun findUserById(id: Int): FullUser? {
        return transaction {
            Users
                .selectAll()
                .where { Users.id eq id }
                .map { it.toFullUser() }
                .singleOrNull()
        }
    }

    fun findUsersByQuery(query: String): List<FullUser> {
        val pattern = "${query.lowercase()}%"
        return transaction {
            Users
                .selectAll().where { Users.username.lowerCase() like pattern }
                .map { it.toFullUser() }
        }
    }

    fun changeUserVerification(id: Int): Boolean {
        return transaction {
            Users.update({ Users.id eq id }) {
                it[isVerified] = true
            } > 0
        }
    }

    fun changePassword(id: Int, newPassword: String): Boolean {
        val safePassword = PasswordHasher.hashPassword(newPassword)
        return transaction {
            Users.update({ Users.id eq id }) {
                it[hashedPassword] = safePassword
            } > 0
        }

    }
}
