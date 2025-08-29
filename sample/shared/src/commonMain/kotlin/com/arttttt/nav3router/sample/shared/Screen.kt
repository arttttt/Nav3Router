package com.arttttt.nav3router.sample.shared

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen : NavKey {

    @Serializable
    data class Main(
        val index: Int,
    ) : Screen
}