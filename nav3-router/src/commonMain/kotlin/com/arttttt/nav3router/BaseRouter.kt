package com.arttttt.nav3router

abstract class BaseRouter<T : Any> {

    internal val commandQueue = CommandQueue<T>()

    protected fun executeCommands(vararg commands: Command<T>) {
        commandQueue.executeCommands(commands)
    }
}