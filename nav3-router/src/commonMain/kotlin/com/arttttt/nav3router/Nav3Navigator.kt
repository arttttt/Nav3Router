package com.arttttt.nav3router

import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey

class Nav3Navigator(
    private val navBackStack: SnapshotStateList<NavKey>,
    private val onBack: () -> Unit,
) : Navigator<NavKey> {

    override fun applyCommands(
        commands: Array<out Command<NavKey>>,
    ) {
        val snapshot = navBackStack.toMutableList()

        for (command in commands) {
            try {
                applyCommand(
                    snapshot = snapshot,
                    command = command,
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
    ) {
        when (command) {
            is Push<NavKey> -> forward(
                snapshot = snapshot,
                command = command,
            )
            is ReplaceCurrent<NavKey> -> replace(
                snapshot = snapshot,
                command = command,
            )
            is PopTo<NavKey> -> backTo(
                snapshot = snapshot,
                command = command,
            )
            is Pop -> back(
                snapshot = snapshot,
            )
        }
    }

    private fun forward(
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

    private fun backTo(
        snapshot: MutableList<NavKey>,
        command: PopTo<NavKey>,
    ) {
        val target = command.screen
        if (target == null) {
            if (snapshot.size > 1) {
                snapshot.removeRange(1, snapshot.size)
            }
            return
        }

        val idx = snapshot.indexOfFirst { it == command.screen }

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

    private fun back(
        snapshot: MutableList<NavKey>,
    ) {
        if (snapshot.isEmpty()) return

        if (snapshot.size > 1) {
            snapshot.removeLastOrNull()
        } else {
            onBack()
        }
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