package com.arttttt.nav3router

/**
 * Core interface for navigation execution.
 *
 * Navigator is responsible for applying navigation commands to the actual navigation stack.
 * It acts as a bridge between the high-level Router API and the underlying navigation system.
 *
 * @param T The type of destinations/screens that this navigator can handle
 */
interface Navigator<in T : Any> {

    /**
     * Applies an array of navigation commands to the navigation stack.
     *
     * @param commands Array of commands to apply to the navigation stack
     */
    fun applyCommands(commands: Array<out Command<T>>)
}