package com.arttttt.nav3router

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Manages queuing and execution of navigation commands.
 *
 * CommandQueue solves the timing problem where navigation commands might be issued
 * before the navigation system is ready(e.g. changing configuration).
 * It stores commands when no navigator is available and executes them
 * immediately when a navigator becomes available.
 *
 * All command execution happens on the Main dispatcher to ensure UI thread safety.
 */
class CommandQueue<T : Any> : NavigatorHolder<T> {

    /** Currently registered navigator, null if none is set */
    private var navigator: Navigator<T>? = null

    /** Queue of pending commands waiting for a navigator to become available */
    private val pendingCommands = mutableListOf<Array<out Command<T>>>()

    /** Coroutine scope for executing commands on the main thread */
    private val mainScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    /**
     * Sets the navigator and executes any pending commands.
     *
     * When a navigator is registered, all queued commands are immediately
     * executed in the order they were added to the queue.
     *
     * @param navigator The navigator to register
     */
    override fun setNavigator(navigator: Navigator<T>) {
        this.navigator = navigator
        if (pendingCommands.isNotEmpty()) {
            val snapshot = pendingCommands.toList()
            pendingCommands.clear()
            snapshot.forEach { navigator.applyCommands(it) }
        }
    }

    /**
     * Removes the current navigator.
     *
     * After calling this, new commands will be queued until a new navigator is set.
     */
    override fun removeNavigator() {
        navigator = null
    }

    /**
     * Executes navigation commands immediately or queues them if no navigator is available.
     *
     * This method ensures that commands are always executed on the main thread,
     * making it safe to call from any thread context.
     *
     * @param commands Array of commands to execute
     */
    fun executeCommands(commands: Array<out Command<T>>) {
        mainScope.launch {
            navigator?.applyCommands(commands) ?: pendingCommands.add(commands)
        }
    }
}