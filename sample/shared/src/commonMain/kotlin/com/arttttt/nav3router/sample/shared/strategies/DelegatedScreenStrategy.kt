package com.arttttt.nav3router.sample.shared.strategies

import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope

class DelegatedScreenStrategy<T : Any>(
    private val strategyMap: Map<String, SceneStrategy<T>>,
    private val fallbackStrategy: SceneStrategy<T>? = null
) : SceneStrategy<T> {

    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val lastEntry = entries.lastOrNull() ?: return null

        for (key in lastEntry.metadata.keys) {
            val strategy = strategyMap[key]
            if (strategy != null) {
                return strategy.run { calculateScene(entries) }
            }
        }

        return fallbackStrategy?.run { calculateScene(entries) }
    }
}