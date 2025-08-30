package com.arttttt.nav3router

interface Navigator<in T : Any> {
    fun applyCommands(commands: Array<out Command<T>>)
}