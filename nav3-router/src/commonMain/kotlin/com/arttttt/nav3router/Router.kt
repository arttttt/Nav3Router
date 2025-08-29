package com.arttttt.nav3router

class Router<in T : Any> {

    internal val commandQueue = CommandQueue()

    fun push(vararg screens: T) {
        if (screens.isEmpty()) error("Screens must not be empty")

        executeCommands(
            *screens
                .map { screen -> Push(screen) }
                .toTypedArray()
        )
    }

    fun replaceCurrent(screen: T) {
        executeCommands(ReplaceCurrent(screen))
    }

    fun popUpTo(screen: T?) {
        executeCommands(PopTo(screen))
    }

    fun replaceStack(vararg screens: T) {
        if (screens.isEmpty()) error("Screens must not be empty")

        executeCommands(
            PopTo<T>(null),
            *screens
                .mapIndexed { index, screen ->
                    if (index == 0) {
                        ReplaceCurrent(screen)
                    } else {
                        Push(screen)
                    }
                }
                .toTypedArray()
        )
    }

    fun clearStack() {
        executeCommands(
            PopTo<T>(null),
            Pop,
        )
    }

    fun popUp() {
        executeCommands(Pop)
    }

    private fun executeCommands(vararg commands: Command) {
        commandQueue.executeCommands(commands)
    }
}