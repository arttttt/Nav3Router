package com.arttttt.nav3router

interface Command<out T : Any>

data class Push<T : Any>(val screen: T) : Command<T>
data class ReplaceCurrent<T : Any>(val screen: T) : Command<T>
data object Pop : Command<Nothing>
data class PopTo<T : Any>(val screen: T?) : Command<T>