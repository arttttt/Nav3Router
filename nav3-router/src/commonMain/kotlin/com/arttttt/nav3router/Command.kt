package com.arttttt.nav3router

interface Command

data class Forward<T : Any>(val screen: T) : Command

data class Replace<T : Any>(val screen: T) : Command

data object Back : Command

data class BackTo<T : Any>(val screen: T?) : Command