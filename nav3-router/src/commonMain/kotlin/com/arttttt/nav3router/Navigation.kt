package com.arttttt.nav3router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

/**
 * CompositionLocal that provides access to the parent Router in nested navigation hierarchies.
 *
 * This is used to establish a parent-child relationship between navigation containers.
 * When a nested Nav3Host is created, it can access its parent router through this CompositionLocal
 * to properly handle navigation events like pop() and dropStack().
 *
 * Value is null for root navigation containers (no parent exists).
 * Value is non-null for nested navigation containers (parent router available).
 *
 * Users typically don't need to access this directly - it's managed automatically by Nav3Host.
 */
internal val LocalParentRouter = compositionLocalOf<Router<*>?> { null }

/**
 * Creates and remembers a Router instance.
 *
 * The router will be recreated if any of the provided keys change, allowing for
 * state-dependent router configurations.
 *
 * @param keys Optional keys that trigger router recreation when changed
 * @param factory Factory function to create the router instance
 * @return A remembered router instance
 */
@Composable
fun <T : Any> rememberRouter(
    vararg keys: Any?,
    factory: () -> Router<T> = { Router() },
): Router<T> {
    return remember(*keys) { factory() }
}

/**
 * Creates and remembers a Nav3Navigator bound to the given back stack.
 *
 * The navigator will be recreated only if the back stack reference or onBack callback changes.
 * This ensures proper lifecycle management and prevents memory leaks.
 *
 * @param backStack The Navigation 3 back stack to control
 * @param onBack Callback triggered when system back navigation is needed
 * @return A remembered navigator instance
 */
@Composable
fun rememberNav3Navigator(
    backStack: NavBackStack<NavKey>,
    onBack: () -> Unit,
): Navigator<NavKey> {
    return remember(backStack, onBack) { Nav3Navigator(backStack, onBack) }
}

/**
 * Main composable for setting up Navigation 3 integration.
 *
 * This composable:
 * 1. Connects the router to the navigator for command execution
 * 2. Provides proper lifecycle management (setup/cleanup)
 * 3. Creates a standardized onBack callback for UI components
 *
 * The connection between router and navigator is automatically managed through
 * DisposableEffect, ensuring proper cleanup when the composable leaves the composition.
 *
 * @param backStack The Navigation 3 back stack
 * @param router The router instance for issuing navigation commands
 * @param navigator The navigator instance for executing commands (auto-created if not provided)
 * @param content The content composable that receives the navigation setup
 */
@Composable
fun <T : NavKey> Nav3Host(
    backStack: NavBackStack<NavKey>,
    router: Router<T> = rememberRouter(),
    navigator: Navigator<T> = rememberNav3Navigator(
        backStack = backStack,
        onBack = LocalParentRouter.current?.let { it::pop } ?: platformOnBack(),
    ),
    content: @Composable (
        backStack: NavBackStack<NavKey>,
        onBack: () -> Unit,
        router: Router<T>,
    ) -> Unit,
) {
    DisposableEffect(router, navigator) {
        router.commandQueue.setNavigator(navigator)
        onDispose { router.commandQueue.removeNavigator() }
    }

    val onBack: () -> Unit = remember(router) {
        { router.pop() }
    }

    val interceptionEnabled = LocalParentRouter.current != null

    CompositionLocalProvider(LocalParentRouter provides router) {
        BackInterceptionProvider(
            interceptionEnabled = interceptionEnabled,
        ) {
            content(backStack, onBack, router)
        }
    }
}