package com.arttttt.nav3router.result

import androidx.compose.runtime.snapshotFlow
import androidx.navigation3.runtime.NavKey
import com.arttttt.nav3router.Router
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

/**
 * Registers a durable, type-safe handler for results of type [R].
 *
 * This is the core consumer API. The result is addressed by [R]'s serial name, deposited into the
 * host's saveable store by [popWithResult] / [sendResult], and delivered here exactly once. Because
 * only serializable data is persisted and the callback is simply re-attached when the registration
 * site re-runs (a ViewModel re-created, a DisposableEffect re-entered), it survives configuration
 * change **and** process death — no callback is ever serialized.
 *
 * [onCancelled] is best-effort: it fires when navigation returns past the depth at which the
 * registration was made without a result having been delivered. For long-lived registrations it may
 * also react to unrelated back navigation; the [openForResult] sugar scopes it precisely.
 *
 * @return a [ResultRegistration]; call [ResultRegistration.dispose] to stop receiving results.
 */
inline fun <reified R> Router<out NavKey>.registerForResult(
    noinline onCancelled: () -> Unit = {},
    noinline onResult: (R) -> Unit,
): ResultRegistration = registerForResult(serializer<R>(), onCancelled, onResult)

fun <R> Router<out NavKey>.registerForResult(
    serializer: KSerializer<R>,
    onCancelled: () -> Unit = {},
    onResult: (R) -> Unit,
): ResultRegistration {
    val coordinator = resultCoordinator ?: return ResultRegistration {}
    val key = serializer.descriptor.serialName
    val store = coordinator.store
    val backStack = coordinator.backStack
    val baseDepth = backStack.size

    val job = coordinator.scope.launch {
        var opened = false
        snapshotFlow { store.has(key) to backStack.size }
            .distinctUntilChanged()
            .collect { (hasResult, depth) ->
                when {
                    hasResult -> {
                        val encoded = store.take(key)
                        if (encoded != null) {
                            opened = false
                            onResult(ResultStore.json.decodeFromString(serializer, encoded))
                        }
                    }
                    depth > baseDepth -> opened = true
                    opened -> {
                        opened = false
                        onCancelled()
                    }
                }
            }
    }

    return ResultRegistration { job.cancel() }
}

/**
 * Sends [value] back to whoever registered a [registerForResult] handler for type [R], without
 * popping. Use it from a "decider" screen that forwards a result while staying on the stack.
 * No-op if no [com.arttttt.nav3router.Nav3Host] is attached.
 */
inline fun <reified R> Router<out NavKey>.sendResult(value: R) {
    sendResult(serializer<R>(), value)
}

fun <R> Router<out NavKey>.sendResult(serializer: KSerializer<R>, value: R) {
    val store = resultCoordinator?.store ?: return
    store.store(
        requestId = serializer.descriptor.serialName,
        value = ResultStore.json.encodeToString(serializer, value),
    )
}

/**
 * Returns [value] to whoever registered a [registerForResult] handler for type [R], then pops the
 * current screen.
 */
inline fun <reified R> Router<out NavKey>.popWithResult(value: R) {
    sendResult(value)
    pop()
}
