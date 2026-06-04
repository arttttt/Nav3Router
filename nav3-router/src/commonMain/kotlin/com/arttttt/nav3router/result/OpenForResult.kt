package com.arttttt.nav3router.result

import androidx.navigation3.runtime.NavKey
import com.arttttt.nav3router.Router
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import kotlin.coroutines.resume

/**
 * Opens the screen built by [screen] and suspends until a result of type [R] is returned, or `null`
 * if the screen leaves the back stack without one.
 *
 * Ergonomic sugar over [registerForResult] wrapped in `suspendCancellableCoroutine`.
 */
@EphemeralResultApi
suspend inline fun <reified R> Router<out NavKey>.openForResult(
    noinline screen: () -> NavKey,
): R? = openForResult(serializer<R>(), screen)

@EphemeralResultApi
@PublishedApi
internal suspend fun <R> Router<out NavKey>.openForResult(
    serializer: KSerializer<R>,
    screen: () -> NavKey,
): R? = suspendCancellableCoroutine { continuation ->
    var registration: ResultRegistration? = null
    registration = registerForResult(
        serializer = serializer,
        onResult = { value ->
            registration?.dispose()
            if (continuation.isActive) continuation.resume(value)
        },
        onCancelled = {
            registration?.dispose()
            if (continuation.isActive) continuation.resume(null)
        },
    )
    continuation.invokeOnCancellation { registration?.dispose() }

    @Suppress("UNCHECKED_CAST")
    (this as Router<NavKey>).push(screen())
}

/**
 * Cold [Flow] of results of type [R] — sugar over [registerForResult]. Registers on collection and
 * disposes when the collector stops.
 */
@EphemeralResultApi
inline fun <reified R> Router<out NavKey>.resultFlow(): Flow<R> = resultFlow(serializer<R>())

@EphemeralResultApi
@PublishedApi
internal fun <R> Router<out NavKey>.resultFlow(
    serializer: KSerializer<R>,
): Flow<R> = callbackFlow {
    val registration = registerForResult(
        serializer = serializer,
        onResult = { trySend(it) },
    )
    awaitClose { registration.dispose() }
}
