package com.arttttt.nav3router.sample.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
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
typealias NavBackStack = SnapshotStateList<NavKey>

/**
 * Remove when [androidx.navigation3:navigation3-ui] will become multiplatform
 */
@Composable
expect inline fun <reified T : NavKey> rememberNavBackStack(vararg elements: T): NavBackStack

/**
 * Remove when [androidx.navigation3:navigation3-ui] will become multiplatform
 */
expect interface SceneStrategy<T : Any>

/**
 * Remove when [androidx.navigation3:navigation3-ui] will become multiplatform
 */
expect class SinglePaneSceneStrategy<T : Any>() : SceneStrategy<T>

/**
 * Remove when [androidx.navigation3:navigation3-ui] will become multiplatform
 */
expect class DialogSceneStrategy<T : Any>() : SceneStrategy<T> {

     companion object
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