package com.arttttt.nav3router.test

import androidx.navigation3.runtime.NavKey
import com.arttttt.nav3router.Command
import com.arttttt.nav3router.Navigator

/**
 * A [Navigator] that records every applied [Command] instead of mutating a back stack — for
 * asserting *which* commands a [com.arttttt.nav3router.Router] emitted, independently of the
 * resulting stack. Attach it via `router.bindForTest(RecordingNavigator())`.
 */
class RecordingNavigator : Navigator<NavKey> {

    private val recorded = mutableListOf<Command<NavKey>>()

    /** All commands applied so far, in order. */
    val commands: List<Command<NavKey>> get() = recorded.toList()

    override fun applyCommands(commands: Array<out Command<NavKey>>) {
        recorded += commands
    }
}
