package com.arttttt.nav3router

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform