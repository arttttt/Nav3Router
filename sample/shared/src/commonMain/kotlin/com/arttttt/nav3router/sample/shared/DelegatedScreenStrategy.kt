package com.arttttt.nav3router.sample.shared

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry

class DelegatedScreenStrategy<T : Any>(
    private val strategyMap: Map<String, SceneStrategy<T>>,
    private val fallbackStrategy: SceneStrategy<T>? = null
) : SceneStrategy<T> {

    @Composable
    override fun calculateScene(
        entries: List<NavEntry<T>>,
        onBack: (Int) -> Unit
    ): Scene<T>? {
        val lastEntry = entries.lastOrNull() ?: return null

        for (key in lastEntry.metadata.keys) {
            val strategy = strategyMap[key]
            if (strategy != null) {
                return strategy.calculateScene(entries, onBack)
            }
        }

        return fallbackStrategy?.calculateScene(entries, onBack)
    }
}