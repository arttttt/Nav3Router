package com.arttttt.nav3router.test

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.arttttt.nav3router.Nav3Navigator
import com.arttttt.nav3router.Navigator
import com.arttttt.nav3router.Router
import kotlinx.coroutines.CoroutineScope

/**
 * Binds this [Router] to a [Nav3Navigator] over [backStack] (plus a result store) **without** a
 * Compose `Nav3Host`, so navigation can be driven from plain unit tests. Returns the live
 * [backStack] to assert on.
 *
 * Commands still execute on `Dispatchers.Main`, so call this from a test that installs a test main
 * dispatcher and advance the scheduler before asserting. [scope] backs result delivery; [onBack] is
 * invoked when system back is requested.
 */
@OptIn(InternalNavTestApi::class)
fun <T : NavKey> Router<T>.bindForTest(
    backStack: NavBackStack<NavKey> = NavBackStack(),
    scope: CoroutineScope,
    onBack: () -> Unit = {},
): NavBackStack<NavKey> {
    setTestNavigator(Nav3Navigator(backStack, onBack))
    installTestResultStore(backStack, scope)
    return backStack
}

/**
 * Binds this [Router] to a custom [navigator] — typically a [RecordingNavigator] for asserting
 * which commands are emitted, independently of the resulting stack.
 */
@OptIn(InternalNavTestApi::class)
fun <T : NavKey> Router<T>.bindForTest(navigator: Navigator<T>) {
    setTestNavigator(navigator)
}
