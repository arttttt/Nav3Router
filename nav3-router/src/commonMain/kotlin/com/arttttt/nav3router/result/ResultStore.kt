package com.arttttt.nav3router.result

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import kotlinx.serialization.json.Json

/**
 * Durable, observable holder for in-flight navigation results.
 *
 * Results are kept as JSON-encoded strings keyed by [ResultKey.requestId]. Because both the key
 * and the encoded value are plain strings, the whole store can be persisted to a single String
 * (see [snapshot]/[Companion.restore]), so a produced-but-not-yet-consumed result survives
 * configuration change and process death on every supported platform — not only Android.
 *
 * The backing map is a [SnapshotStateMap], so observers (result channels) recompose / re-collect
 * when a result is deposited.
 */
class ResultStore internal constructor(
    initial: Map<String, String>,
) {

    internal val results: SnapshotStateMap<String, String> =
        mutableStateMapOf<String, String>().apply { putAll(initial) }

    /** Deposits an already-encoded [value] under [requestId], overwriting any previous one. */
    internal fun store(requestId: String, value: String) {
        results[requestId] = value
    }

    /** Removes and returns the encoded result for [requestId], or `null` if none is pending. */
    internal fun take(requestId: String): String? = results.remove(requestId)

    /** Returns `true` if a result is currently pending for [requestId]. */
    internal fun has(requestId: String): Boolean = results.containsKey(requestId)

    /** Immutable copy of the current contents, used by the saver. */
    internal fun snapshot(): Map<String, String> = results.toMap()

    internal companion object {

        val json: Json = Json {
            ignoreUnknownKeys = true
        }

        fun empty(): ResultStore = ResultStore(emptyMap())

        fun restore(contents: Map<String, String>): ResultStore = ResultStore(contents)
    }
}
