package com.arttttt.nav3router

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey

// TODO: do not mutate a whole list for each command
class Nav3Navigator(
    private val navBackStack: SnapshotStateList<NavKey>,
    private val onBack: () -> Unit,
) : Navigator<NavKey> {

    override fun applyCommands(
        commands: Array<out Command<NavKey>>,
    ) {
        for (command in commands) {
            try {
                applyCommand(command)
            } catch (e: RuntimeException) {
                // TODO: handle exceptions
            }
        }
    }

    private fun applyCommand(command: Command<NavKey>) {
        when (command) {
            is Push<NavKey> -> forward(command)
            is ReplaceCurrent<NavKey> -> replace(command)
            is PopTo<NavKey> -> backTo(command)
            is Pop -> back()
        }
    }

    private fun forward(command: Push<NavKey>) {
        navBackStack += command.screen
    }

    private fun replace(command: ReplaceCurrent<NavKey>) {
        if (navBackStack.isEmpty()) {
            navBackStack += command.screen
        } else {
            navBackStack.set(
                index = navBackStack.indices.last,
                element = command.screen,
            )
        }
    }

    private fun backTo(command: PopTo<NavKey>) {
        val target = command.screen
        if (target == null) {
            if (navBackStack.size > 1) {
                navBackStack.removeRange(1, navBackStack.size)
            }
            return
        }

        val idx = navBackStack.indexOfFirst { it == command.screen }

        if (idx == -1) {
            if (navBackStack.size > 1) {
                navBackStack.removeRange(1, navBackStack.size)
            }
        } else {
            val from = idx + 1
            if (from < navBackStack.size) {
                navBackStack.removeRange(from, navBackStack.size)
            }
        }
    }

    private fun back() {
        if (navBackStack.isEmpty()) return

        if (navBackStack.size > 1) {
            navBackStack.removeLastOrNull()
        } else {
            onBack()
        }
    }
}