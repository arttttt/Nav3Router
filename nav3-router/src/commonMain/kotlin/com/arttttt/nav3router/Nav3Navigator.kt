package com.arttttt.nav3router

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey

// TODO: do not mutate a whole list for each command
class Nav3Navigator(
    private val navBackStack: SnapshotStateList<NavKey>,
) : Navigator {

    override fun applyCommands(
        commands: Array<out Command>,
    ) {
        for (command in commands) {
            try {
                applyCommand(command)
            } catch (e: RuntimeException) {
                // TODO: handle exceptions
            }
        }
    }

    private fun applyCommand(command: Command) {
        when (command) {
            is Push<*> -> forward(command)
            is ReplaceCurrent<*> -> replace(command)
            is PopTo<*> -> backTo(command)
            is Pop -> back()
        }
    }

    private fun forward(command: Push<*>) {
        if (command.screen !is NavKey) error("Screen must be NavKey")

        navBackStack += command.screen
    }

    private fun replace(command: ReplaceCurrent<*>) {
        if (command.screen !is NavKey) error("Screen must be NavKey")

        if (navBackStack.isEmpty()) {
            navBackStack += command.screen
        } else {
            navBackStack.set(
                index = navBackStack.indices.last,
                element = command.screen,
            )
        }
    }

    private fun backTo(command: PopTo<*>) {
        val target = command.screen
        if (target == null) {
            if (navBackStack.size > 1) {
                navBackStack.removeRange(1, navBackStack.size)
            }
            return
        }

        if (command.screen !is NavKey) error("Screen must be NavKey")
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

        navBackStack.removeLastOrNull()
    }
}