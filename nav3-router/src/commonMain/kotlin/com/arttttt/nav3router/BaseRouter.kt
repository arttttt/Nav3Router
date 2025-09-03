package com.arttttt.nav3router

/**
 * Base class for all router implementations.
 *
 * Provides common functionality for managing navigation commands through a command queue system.
 * Subclasses implement specific navigation methods that create and execute appropriate commands.
 *
 * @param T The type of destinations/screens that this router can handle
 */
abstract class BaseRouter<T : Any> {

    /**
     * Command queue that manages command execution and navigator lifecycle.
     *
     * This is internal to prevent direct access from outside the library,
     * but accessible to the navigation system for setup.
     */
    internal val commandQueue = CommandQueue<T>()

    /**
     * Executes one or more navigation commands.
     *
     * This is the primary method for triggering navigation operations.
     * Commands are passed to the command queue, which handles timing and execution.
     *
     * @param commands Variable number of commands to execute
     */
    protected fun executeCommands(vararg commands: Command<T>) {
        commandQueue.executeCommands(commands)
    }
}