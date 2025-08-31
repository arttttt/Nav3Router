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

actual interface SceneStrategy<T : Any> {

    @Composable
    actual fun calculateScene(
        entries: List<NavEntry<T>>,
        onBack: (Int) -> Unit
    ): Scene<T>?
}

actual class SinglePaneSceneStrategy<T : Any> : SceneStrategy<T> {

    @Composable
    actual override fun calculateScene(
        entries: List<NavEntry<T>>,
        onBack: (Int) -> Unit,
    ): Scene<T> {
        TODO("Not yet implemented")
    }
}

actual class DialogSceneStrategy<T : Any> : SceneStrategy<T> {

    actual companion object

    @Composable
    actual override fun calculateScene(
        entries: List<NavEntry<T>>,
        onBack: (Int) -> Unit,
    ): Scene<T>? {
        TODO("Not yet implemented")
    }
}

actual interface Scene<T : Any> {
    actual val key: Any
    actual val entries: List<NavEntry<T>>
    actual val previousEntries: List<NavEntry<T>>
    actual val overlaidEntries: List<NavEntry<T>>
    actual val content: @Composable (() -> Unit)
}

actual interface OverlayScene<T : Any> : Scene<T>