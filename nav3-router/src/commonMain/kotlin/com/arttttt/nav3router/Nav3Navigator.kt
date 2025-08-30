package com.arttttt.nav3router

import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

class Nav3Navigator(
    private val navBackStack: SnapshotStateList<NavKey>,
    private val onBack: () -> Unit,
) : Navigator<NavKey> {

    private val mainScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun applyCommands(
        commands: Array<out Command<NavKey>>,
    ) {
        val snapshot = navBackStack.toMutableList()

        for (command in commands) {
            try {
                applyCommand(
                    snapshot = snapshot,
                    command = command,
                    onBackRequested = {
                        mainScope.launch {
                            yield()
                            onBack()
                        }
                    },
                )
            } catch (e: RuntimeException) {
                // TODO: handle exceptions
            }
        }

        navBackStack.swap(snapshot)
    }

    private fun applyCommand(
        snapshot: MutableList<NavKey>,
        command: Command<NavKey>,
        onBackRequested: () -> Unit,
    ) {
        when (command) {
            is Push<NavKey> -> push(
                snapshot = snapshot,
                command = command,
            )
            is ReplaceCurrent<NavKey> -> replace(
                snapshot = snapshot,
                command = command,
            )
            is PopTo<NavKey> -> popTo(
                snapshot = snapshot,
                command = command,
            )
            is Pop -> {
                if (!pop(snapshot)) onBackRequested()
            }
            is ResetToRoot -> resetToRoot(
                snapshot = snapshot,
            )
        }
    }

    private fun push(
        snapshot: MutableList<NavKey>,
        command: Push<NavKey>,
    ) {
        snapshot += command.screen
    }

    private fun replace(
        snapshot: MutableList<NavKey>,
        command: ReplaceCurrent<NavKey>,
    ) {
        if (snapshot.isEmpty()) {
            snapshot += command.screen
        } else {
            snapshot.set(
                index = snapshot.indices.last,
                element = command.screen,
            )
        }
    }

    private fun popTo(
        snapshot: MutableList<NavKey>,
        command: PopTo<NavKey>,
    ) {
        val target = command.screen
        val idx = snapshot.indexOfFirst { it == target }

        if (idx == -1) {
            if (snapshot.size > 1) {
                snapshot.removeRange(1, snapshot.size)
            }
        } else {
            val from = idx + 1
            if (from < snapshot.size) {
                snapshot.removeRange(from, snapshot.size)
            }
        }
    }

    private fun pop(
        snapshot: MutableList<NavKey>,
    ): Boolean {
        if (snapshot.isEmpty()) return true

        val result = snapshot.size > 1

        if (result) {
            snapshot.removeLastOrNull()
        }

        return result
    }

    private fun resetToRoot(snapshot: MutableList<NavKey>) {
        if (snapshot.size > 1) snapshot.removeRange(1, snapshot.size)
    }

    private fun SnapshotStateList<NavKey>.swap(
        value: List<NavKey>,
    ) {
        Snapshot.withMutableSnapshot {
            clear()
            addAll(value)
        }
    }

    private fun MutableList<NavKey>.removeRange(fromIndex: Int, toIndex: Int) {
        subList(fromIndex, toIndex).clear()
    }
}