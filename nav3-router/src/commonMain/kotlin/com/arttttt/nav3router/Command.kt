package com.arttttt.nav3router

interface Command

data class Push<T : Any>(val screen: T) : Command
data class ReplaceCurrent<T : Any>(val screen: T) : Command
data object Pop : Command
data class PopTo<T : Any>(val screen: T?) : Command