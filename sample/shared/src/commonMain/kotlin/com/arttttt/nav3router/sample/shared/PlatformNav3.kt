package com.arttttt.nav3router.sample.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey

/**
 * Remove when [androidx.navigation3:navigation3-ui] will become multiplatform
 */
@Composable
expect fun <T : Any> NavDisplay(
    backStack: List<T>,
    modifier: Modifier,
    onBack: (Int) -> Unit,
    sceneStrategy: SceneStrategy<T> = SinglePaneSceneStrategy(),
    entryProvider: (key: T) -> NavEntry<T>,
)

/**
 * Remove when [androidx.navigation3:navigation3-ui] will become multiplatform
 */
typealias NavBackStack = androidx.navigation3.runtime.NavBackStack<NavKey>

/**
 * Remove when [androidx.navigation3:navigation3-ui] will become multiplatform
 */
@Composable
expect inline fun <reified T : NavKey> rememberNavBackStack(vararg elements: T): NavBackStack

/**
 * Remove when [androidx.navigation3:navigation3-ui] will become multiplatform
 */
expect interface SceneStrategy<T : Any> {

    @Composable
    fun calculateScene(entries: List<NavEntry<T>>, onBack: (count: Int) -> Unit): Scene<T>?
}

/**
 * Remove when [androidx.navigation3:navigation3-ui] will become multiplatform
 */
expect class SinglePaneSceneStrategy<T : Any>() : SceneStrategy<T> {

    @Composable
    override fun calculateScene(
        entries: List<NavEntry<T>>,
        onBack: (Int) -> Unit,
    ): Scene<T>
}

/**
 * Remove when [androidx.navigation3:navigation3-ui] will become multiplatform
 */
expect class DialogSceneStrategy<T : Any>() : SceneStrategy<T> {

    @Composable
    override fun calculateScene(
        entries: List<NavEntry<T>>,
        onBack: (Int) -> Unit,
    ): Scene<T>?

    companion object
}

/**
 * Remove when [androidx.navigation3:navigation3-ui] will become multiplatform
 */
expect interface Scene<T : Any> {

    val key: Any
    val entries: List<NavEntry<T>>
    val previousEntries: List<NavEntry<T>>
    val content: @Composable () -> Unit
}

/**
 * Remove when [androidx.navigation3:navigation3-ui] will become multiplatform
 */
expect interface OverlayScene<T : Any> : Scene<T> {

    val overlaidEntries: List<NavEntry<T>>
}

/**
 * Remove when [androidx.navigation3:navigation3-ui] will become multiplatform
 */
private const val DIALOG_KEY = "dialog"

/**
 * Remove when [androidx.navigation3:navigation3-ui] will become multiplatform
 */
fun DialogSceneStrategy.Companion.dialog(
    dialogProperties: DialogProperties = DialogProperties()
): Map<String, Any> {
    return mapOf(DIALOG_KEY to dialogProperties)
}