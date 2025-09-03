package com.arttttt.nav3router

/**
 * Interface for managing the lifecycle of Navigator instances.
 *
 * NavigatorHolder is used to decouple navigation command generation from navigation execution.
 * This allows commands to be queued when no navigator is available and executed later
 * when a navigator becomes available.
 *
 * @param T The type of destinations/screens
 */
interface NavigatorHolder<T : Any> {

    /**
     * Registers a navigator instance to handle navigation commands.
     *
     * @param navigator The navigator instance to register
     */
    fun setNavigator(navigator: Navigator<T>)

    /**
     * Unregisters the current navigator.
     */
    fun removeNavigator()
}