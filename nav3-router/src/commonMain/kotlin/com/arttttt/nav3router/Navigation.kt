package com.arttttt.nav3router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey

/**
 * Remember a Router<T>. By default creates a fresh Router().
 * You can pass keys to recreate on change, or a custom factory.
 */
@Composable
fun <T : Any> rememberRouter(
    vararg keys: Any?,
    factory: () -> Router<T> = { Router() },
): Router<T> {
    return remember(*keys) { factory() }
}

/**
 * Remember a Nav3Navigator bound to the given backStack.
 * Re-created only if backStack reference changes.
 */
@Composable
fun rememberNav3Navigator(
    backStack: SnapshotStateList<NavKey>,
    onBack: () -> Unit,
): Navigator<NavKey> {
    return remember(backStack, onBack) { Nav3Navigator(backStack, onBack) }
}

@Composable
fun <T : NavKey> Nav3Host(
    backStack: SnapshotStateList<NavKey>,
    router: Router<T> = rememberRouter(),
    navigator: Navigator<T> = rememberNav3Navigator(
        backStack = backStack,
        onBack = platformOnBack(),
    ),
    content: @Composable (
        backStack: SnapshotStateList<NavKey>,
        onBack: (Int) -> Unit,
        router: Router<T>,
    ) -> Unit,
) {
    DisposableEffect(router, navigator) {
        router.commandQueue.setNavigator(navigator)
        onDispose { router.commandQueue.removeNavigator() }
    }

    val onBack: (Int) -> Unit = remember(router) {
        { steps -> repeat(steps) { router.popUp() } }
    }

    content(backStack, onBack, router)
}