package com.arttttt.nav3router

interface Navigator {
    fun applyCommands(commands: Array<out Command>)
}