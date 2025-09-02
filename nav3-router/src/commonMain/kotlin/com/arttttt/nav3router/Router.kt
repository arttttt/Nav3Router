package com.arttttt.nav3router

open class Router<T : Any> : BaseRouter<T>() {

    fun push(vararg screens: T) {
        if (screens.isEmpty()) error("Screens must not be empty")

        val commands = screens.map(::Push).toTypedArray<Command<T>>()
        commandQueue.executeCommands(commands)
    }

    fun replaceCurrent(screen: T) {
        executeCommands(ReplaceCurrent(screen))
    }

    fun replaceStack(vararg screens: T) {
        require(screens.isNotEmpty()) { "Screens must not be empty" }
        val commands = buildList {
            add(ResetToRoot)
            screens.mapIndexedTo(this) { index, screen ->
                if (index == 0) {
                    ReplaceCurrent(screen)
                } else {
                    Push(screen)
                }
            }
        }
        commandQueue.executeCommands(commands.toTypedArray())
    }

    fun clearStack() {
        executeCommands(ResetToRoot)
    }

    fun dropStack() {
        executeCommands(DropStack)
    }

    fun pop() {
        executeCommands(Pop)
    }

    fun popTo(screen: T) {
        executeCommands(PopTo(screen))
    }
}