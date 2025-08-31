package com.arttttt.nav3router.sample.shared

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey

@Composable
actual fun <T : Any> NavDisplay(
    backStack: List<T>,
    modifier: androidx.compose.ui.Modifier,
    onBack: (Int) -> Unit,
    sceneStrategy: SceneStrategy<T>,
    entryProvider: (T) -> NavEntry<T>,
) {
    TODO("Not yet implemented")
}

@Composable
actual inline fun <reified T : NavKey> rememberNavBackStack(vararg elements: T): NavBackStack {
    TODO("Not yet implemented")
}

actual interface SceneStrategy<T : Any>

actual class SinglePaneSceneStrategy<T : Any> : SceneStrategy<T>
actual class DialogSceneStrategy<T : Any> : SceneStrategy<T>