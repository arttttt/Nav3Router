package com.arttttt.nav3router

import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

open class Nav3Navigator(
    private val navBackStack: SnapshotStateList<NavKey>,
    private val onBack: () -> Unit,
) : Navigator<NavKey> {

    protected val mainScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun applyCommands(
        commands: Array<out Command<NavKey>>,
    ) {
        val snapshot = navBackStack.toMutableList()
        var callOnBack = false

        for (command in commands) {
            try {
                applyCommand(
                    snapshot = snapshot,
                    command = command,
                    onBackRequested = {
                        callOnBack = true
                    },
                )
            } catch (e: RuntimeException) {
                // TODO: handle exceptions
            }
        }

        navBackStack.swap(snapshot)

        if (callOnBack) {
            scheduleOnBack()
        }
    }

    protected open fun applyCommand(
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
            is DropStack -> {
                dropStack(
                    snapshot = snapshot,
                )
                onBackRequested()
            }
        }
    }

    protected open fun push(
        snapshot: MutableList<NavKey>,
        command: Push<NavKey>,
    ) {
        snapshot += command.screen
    }

    protected open fun replace(
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

    protected open fun popTo(
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

    protected open fun pop(
        snapshot: MutableList<NavKey>,
    ): Boolean {
        val result = snapshot.size > 1

        if (result) {
            snapshot.removeLastOrNull()
        }

        return result
    }

    protected open fun resetToRoot(snapshot: MutableList<NavKey>) {
        if (snapshot.size > 1) snapshot.removeRange(1, snapshot.size)
    }

    protected open fun dropStack(snapshot: MutableList<NavKey>) {
        snapshot.removeRange(0, snapshot.size - 1)
    }

    /**
     * [onBack] calls platform code for performing back navigation
     * in case we can't pop the backstack anymore
     *
     * if the stack is not empty before calling [Pop] then
     * it might be a problem that [NavDisplay] does not have an updates list
     *
     * in this case we schedule an [onBack] call and force it to be delayed
     * by calling [yield]
     */
    protected open fun scheduleOnBack() {
        mainScope.launch {
            yield()
            onBack()
        }
    }

    protected fun SnapshotStateList<NavKey>.swap(
        value: List<NavKey>,
    ) {
        Snapshot.withMutableSnapshot {
            clear()
            addAll(value)
        }
    }

    protected fun MutableList<NavKey>.removeRange(fromIndex: Int, toIndex: Int) {
        subList(fromIndex, toIndex).clear()
    }
}