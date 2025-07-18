package com.voidchat

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform