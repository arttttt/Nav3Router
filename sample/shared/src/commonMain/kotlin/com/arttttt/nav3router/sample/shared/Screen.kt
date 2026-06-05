package com.arttttt.nav3router.sample.shared

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kotlin.random.Random

/** A result produced by [Screen.ColorPicker]. */
@Serializable
data class ColorResult(
    val argb: Long,
    val name: String,
)

@Serializable
sealed interface Screen : NavKey {

    @Serializable
    data class Simple(
        val index: Int,
    ) : Screen

    @Serializable
    data object BottomSheet : Screen

    @Serializable
    data object Dialog : Screen

    /** A screen opened for a result; it returns the picked [ColorResult] via popWithResult. */
    @Serializable
    data object ColorPicker : Screen

    @Serializable
    class NestedContainer(
        val key: Int = Random.nextInt(),
    ) : Screen {

        override fun toString(): String {
            return "NestedContainer"
        }
    }
}