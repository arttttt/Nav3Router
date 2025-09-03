package com.arttttt.nav3router

import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

/**
 * Navigator implementation that integrates with Jetpack Navigation 3.
 *
 * This class bridges the gap between the high-level Router API and the underlying
 * Navigation 3 system. It translates navigation commands into direct manipulations
 * of the Navigation 3 back stack.
 *
 * @param navBackStack The Navigation 3 back stack to manipulate
 * @param onBack Callback to trigger system back navigation when the stack is empty
 */
open class Nav3Navigator(
    private val navBackStack: SnapshotStateList<NavKey>,
    private val onBack: () -> Unit,
) : Navigator<NavKey> {

    /** Coroutine scope for scheduling back navigation calls */
    protected val mainScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    /**
     * Applies an array of navigation commands to the back stack.
     *
     * Commands are processed sequentially against a snapshot of the current stack.
     * Once all commands are processed, the actual back stack is updated atomically.
     * This ensures consistency and prevents intermediate states from being visible.
     *
     * @param commands Array of navigation commands to apply
     */
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

    /**
     * Processes a single navigation command against the stack snapshot.
     *
     * This method can be overridden in subclasses to add custom command types
     * or modify the behavior of existing commands.
     *
     * @param snapshot Mutable copy of the navigation stack
     * @param command Command to process
     * @param onBackRequested Callback to trigger when system back navigation is needed
     */
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

    /**
     * Adds a screen to the top of the stack.
     *
     * @param snapshot The stack snapshot to modify
     * @param command The push command containing the screen to add
     */
    protected open fun push(
        snapshot: MutableList<NavKey>,
        command: Push<NavKey>,
    ) {
        snapshot += command.screen
    }

    /**
     * Replaces the current top screen or adds a screen if the stack is empty.
     *
     * @param snapshot The stack snapshot to modify
     * @param command The replace command containing the new screen
     */
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

    /**
     * Removes screens until the target screen is reached.
     *
     * If the target screen is not found, all screens except the root are removed.
     *
     * @param snapshot The stack snapshot to modify
     * @param command The popTo command containing the target screen
     */
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

    /**
     * Removes the top screen from the stack.
     *
     * @param snapshot The stack snapshot to modify
     * @return true if a screen was removed, false if the stack would become empty
     */
    protected open fun pop(
        snapshot: MutableList<NavKey>,
    ): Boolean {
        val result = snapshot.size > 1

        if (result) {
            snapshot.removeLastOrNull()
        }

        return result
    }

    /**
     * Removes all screens except the root.
     *
     * @param snapshot The stack snapshot to modify
     */
    protected open fun resetToRoot(snapshot: MutableList<NavKey>) {
        if (snapshot.size > 1) snapshot.removeRange(1, snapshot.size)
    }

    /**
     * Removes all screens except the current top screen.
     *
     * This function is useful when you want to close a nested navigation graph or
     * a whole application.
     *
     * @param snapshot The stack snapshot to modify
     */
    protected open fun dropStack(snapshot: MutableList<NavKey>) {
        snapshot.removeRange(0, snapshot.size - 1)
    }

    /**
     * Schedules a system back navigation call.
     *
     * The call is delayed using yield() to ensure that NavDisplay has received
     * the updated back stack before the system back navigation is triggered.
     * This prevents race conditions where the system tries to handle back
     * navigation before the UI has been updated.
     */
    protected open fun scheduleOnBack() {
        mainScope.launch {
            yield()
            onBack()
        }
    }

    /**
     * Atomically replaces the contents of a SnapshotStateList.
     *
     * Uses Compose's snapshot system to ensure the update is atomic and
     * properly triggers recomposition.
     *
     * @param value The new contents for the list
     */
    protected fun SnapshotStateList<NavKey>.swap(
        value: List<NavKey>,
    ) {
        Snapshot.withMutableSnapshot {
            clear()
            addAll(value)
        }
    }

    /**
     * Extension function to remove a range of elements from a MutableList.
     *
     * This is a convenience method that uses the subList approach for efficiency.
     *
     * @param fromIndex Start index (inclusive)
     * @param toIndex End index (exclusive)
     */
    protected fun MutableList<NavKey>.removeRange(fromIndex: Int, toIndex: Int) {
        subList(fromIndex, toIndex).clear()
    }
}