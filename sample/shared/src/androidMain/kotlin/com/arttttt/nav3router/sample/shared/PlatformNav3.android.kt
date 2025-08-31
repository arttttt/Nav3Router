package com.arttttt.nav3router.sample.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey

@Composable
actual fun <T : Any> NavDisplay(
    backStack: List<T>,
    modifier: Modifier,
    onBack: (Int) -> Unit,
    sceneStrategy: SceneStrategy<T>,
    entryProvider: (T) -> NavEntry<T>,
) {
    androidx.navigation3.ui.NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = onBack,
        sceneStrategy = sceneStrategy,
        entryProvider = entryProvider,
    )
}

@Composable
actual inline fun <reified T : NavKey> rememberNavBackStack(vararg elements: T): NavBackStack {
    return androidx.navigation3.runtime.rememberNavBackStack(*elements)
}

actual typealias SceneStrategy<T> = androidx.navigation3.ui.SceneStrategy<T>

actual typealias SinglePaneSceneStrategy<T> = androidx.navigation3.ui.SinglePaneSceneStrategy<T>

actual typealias DialogSceneStrategy<T> = androidx.navigation3.ui.DialogSceneStrategy<T>