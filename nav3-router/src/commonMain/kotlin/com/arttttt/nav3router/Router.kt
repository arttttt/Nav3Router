package com.arttttt.nav3router

class Router<in T : Any> {

    internal val commandQueue = CommandQueue()

    fun navigateTo(screen: T) {
        executeCommands(Forward(screen))
    }

    fun newRootScreen(screen: T) {
        executeCommands(
            BackTo(null),
            Replace(screen)
        )
    }

    fun replaceScreen(screen: T) {
        executeCommands(Replace(screen))
    }

    fun backTo(screen: T?) {
        executeCommands(BackTo(screen))
    }

    fun newChain(vararg screens: T) {
        val commands = screens.map { Forward(it) }
        executeCommands(*commands.toTypedArray())
    }

    fun newRootChain(vararg screens: T) {
        val commands = screens.mapIndexed { index, screen ->
            if (index == 0)
                Replace(screen)
            else
                Forward(screen)
        }
        executeCommands(BackTo(null), *commands.toTypedArray())
    }

    fun finishChain() {
        executeCommands(
            BackTo(null),
            Back
        )
    }

    fun back() {
        executeCommands(Back)
    }

    private fun executeCommands(vararg commands: Command) {
        commandQueue.executeCommands(commands)
    }
}