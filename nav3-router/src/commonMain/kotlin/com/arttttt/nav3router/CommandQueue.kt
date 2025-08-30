package com.arttttt.nav3router

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CommandQueue<T : Any> : NavigatorHolder<T> {
    private var navigator: Navigator<T>? = null
    private val pendingCommands = mutableListOf<Array<out Command<T>>>()
    private val mainScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun setNavigator(navigator: Navigator<T>) {
        this.navigator = navigator
        pendingCommands.forEach { navigator.applyCommands(it) }
        pendingCommands.clear()
    }

    override fun removeNavigator() {
        navigator = null
    }

    fun executeCommands(commands: Array<out Command<T>>) {
        mainScope.launch {
            navigator?.applyCommands(commands) ?: pendingCommands.add(commands)
        }
    }
}