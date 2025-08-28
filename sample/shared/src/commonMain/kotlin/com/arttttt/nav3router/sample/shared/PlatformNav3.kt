package com.arttttt.nav3router.sample.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
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