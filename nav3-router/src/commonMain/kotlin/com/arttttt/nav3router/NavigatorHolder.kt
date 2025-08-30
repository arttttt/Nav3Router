package com.arttttt.nav3router

interface NavigatorHolder<T : Any> {
    fun setNavigator(navigator: Navigator<T>)
    fun removeNavigator()
}