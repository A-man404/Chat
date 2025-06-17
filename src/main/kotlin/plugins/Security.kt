package com.example.plugins


import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import table.ROLE
import java.util.*


fun Application.JWTConfig(): JWTConfig {


    val jwt = environment.config.config("ktor.jwt")
    val realm = jwt.property("realm").getString()
    val issuer = jwt.property("issuer").getString()
    val secret = jwt.property("secret").getString()
    val audience = jwt.property("audience").getString()
    val tokenExpiry = jwt.property("expiry").getString()


    return JWTConfig(
        realm = realm,
        secret = secret,
        issuer = issuer,
        audience = audience,
        tokenExpiry = tokenExpiry.toLong()
    )
}

fun Application.configureSecurity(config: JWTConfig) {
    install(Authentication) {
        jwt("jwt-auth") {
            realm = config.realm

            val jwtVerifier =
                JWT.require(Algorithm.HMAC256(config.secret))
                    .withAudience(config.audience)
                    .withIssuer(config.issuer)
                    .build()

            verifier(jwtVerifier)

            validate { jwtCredential ->
                val userId = jwtCredential.payload.getClaim("id").asInt()
                val role = jwtCredential.payload.getClaim("role").asString()
                if (userId != null && role != null) {
                    UserPrincipal(userId, ROLE.valueOf(role))
                } else null
            }

            challenge { defaultScheme, realm ->
                call.respondText("Token is not valid or has expired", status = HttpStatusCode.Unauthorized)

            }
        }
    }
}

fun generateToken(config: JWTConfig, userId: Int?, role: ROLE?): String {
    return JWT.create()
        .withAudience(config.audience)
        .withIssuer(config.issuer)
        .withClaim("id", userId)
        .withClaim("role", role?.name)
        .withExpiresAt(Date(System.currentTimeMillis() + config.tokenExpiry))
        .sign(Algorithm.HMAC256(config.secret))
}


data class JWTConfig(
    val realm: String,
    val secret: String,
    val issuer: String,
    val audience: String,
    val tokenExpiry: Long
)

data class UserPrincipal(val userId: Int, val role: ROLE)