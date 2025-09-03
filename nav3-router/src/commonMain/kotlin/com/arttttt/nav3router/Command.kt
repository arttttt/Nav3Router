package com.arttttt.nav3router

/**
 * Base interface for all navigation commands.
 * Commands represent different navigation operations that can be applied to the navigation stack.
 *
 * @param T The type of screen/destination that this command operates on
 */
interface Command<out T : Any>

/**
 * Command to push a new screen onto the navigation stack.
 * The screen will be added to the top of the stack.
 *
 * @param screen The screen to push onto the stack
 */
data class Push<T : Any>(val screen: T) : Command<T>

/**
 * Command to replace the current top screen with a new one.
 * If the stack is empty, the screen will be added as the first screen.
 *
 * @param screen The screen to replace the current top screen with
 */
data class ReplaceCurrent<T : Any>(val screen: T) : Command<T>

/**
 * Command to pop (remove) the top screen from the navigation stack.
 * If only one screen remains, this will trigger system back navigation.
 */
data object Pop : Command<Nothing>

/**
 * Command to pop all screens until the specified screen is reached.
 * If the target screen is not found, all screens except the root will be removed.
 *
 * @param screen The target screen to navigate back to
 */
data class PopTo<T : Any>(val screen: T) : Command<T>

/**
 * Command to clear all screens except the root screen.
 * This effectively resets the navigation stack to its initial state.
 */
data object ResetToRoot : Command<Nothing>

/**
 * Command to remove all screens except the current top screen.
 * This creates a new stack with only the current screen as the root.
 * After this command, system back navigation will be triggered.
 */
data object DropStack : Command<Nothing>