package com.arttttt.nav3router

/**
 * Main router implementation providing high-level navigation operations.
 *
 * Router offers a convenient API for common navigation patterns like push, pop, replace,
 * and stack manipulation. It translates these operations into appropriate Command objects
 * and executes them through the command queue system.
 *
 * This class is designed to be used as the primary navigation interface in applications.
 *
 * @param T The type of destinations/screens
 */
open class Router<T : Any> : BaseRouter<T>() {

    /**
     * Pushes one or more screens onto the navigation stack.
     *
     * Each screen will be added to the top of the stack in the order provided.
     * This allows for building deep navigation chains in a single operation.
     *
     * @param screens Variable number of screens to push onto the stack
     * @throws IllegalArgumentException if no screens are provided
     */
    fun push(vararg screens: T) {
        require(screens.isNotEmpty()) { "Screens must not be empty" }

        val commands = screens.map(::Push).toTypedArray<Command<T>>()
        commandQueue.executeCommands(commands)
    }

    /**
     * Replaces the current top screen with a new screen.
     *
     * If the stack is empty, the new screen will be added as the first screen.
     * This is useful for scenarios like login flow completion or error recovery.
     *
     * @param screen The screen to replace the current top screen with
     */
    fun replaceCurrent(screen: T) {
        executeCommands(ReplaceCurrent(screen))
    }

    /**
     * Replaces the entire navigation stack with new screens.
     *
     * This operation:
     * 1. Resets to root (keeping only the first screen)
     * 2. Replaces the root screen with the first provided screen
     * 3. Pushes any additional screens on top
     *
     * Useful for major navigation flow changes like switching between authenticated/unauthenticated states.
     *
     * @param screens Variable number of screens to replace the stack with
     * @throws IllegalArgumentException if no screens are provided
     */
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

    /**
     * Clears all screens except the root screen.
     *
     * This resets the navigation stack to its initial state while preserving the root.
     * Useful for "back to main" functionality or clearing complex navigation flows.
     */
    fun clearStack() {
        executeCommands(ResetToRoot)
    }

    /**
     * Removes all screens except the current top screen.
     *
     * The current screen becomes the new root of the stack and
     * system back navigation will be triggered.
     *
     * Use it when you want to close a nested navigation graph or a whole application.
     */
    fun dropStack() {
        executeCommands(DropStack)
    }

    /**
     * Removes the top screen from the navigation stack.
     *
     * If only one screen remains, this will trigger system back navigation
     * (typically exiting the app or returning to the previous activity).
     */
    fun pop() {
        executeCommands(Pop)
    }

    /**
     * Navigates back to a specific screen in the stack.
     *
     * Removes all screens above the target screen. If the target screen is not found,
     * all screens except the root will be removed.
     *
     * @param screen The target screen to navigate back to
     */
    fun popTo(screen: T) {
        executeCommands(PopTo(screen))
    }
}