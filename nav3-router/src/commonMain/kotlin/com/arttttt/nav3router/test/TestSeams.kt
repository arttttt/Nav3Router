package com.arttttt.nav3router.test

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.arttttt.nav3router.Navigator
import com.arttttt.nav3router.Router
import com.arttttt.nav3router.result.ResultCoordinator
import com.arttttt.nav3router.result.ResultStore
import kotlinx.coroutines.CoroutineScope

/**
 * Low-level test seams that reach into Nav3Router's internal wiring (normally established only by a
 * Compose `Nav3Host`). They ship in the main artifact because they need internal access, but are
 * test-only — the `nav3-router-test` artifact builds the friendly `bindForTest` on top of them.
 */

/** Attaches [navigator] to this router's command queue without a Compose `Nav3Host`. */
@InternalNavTestApi
fun <T : NavKey> Router<T>.setTestNavigator(navigator: Navigator<T>) {
    commandQueue.setNavigator(navigator)
}

/** Installs a fresh result store over [backStack] so result delivery works without a `Nav3Host`. */
@InternalNavTestApi
fun <T : NavKey> Router<T>.installTestResultStore(
    backStack: NavBackStack<NavKey>,
    scope: CoroutineScope,
) {
    resultCoordinator = ResultCoordinator(
        store = ResultStore.empty(),
        backStack = backStack,
        scope = scope,
    )
}
