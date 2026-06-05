package com.arttttt.nav3router.result

import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.CoroutineScope

/**
 * Internal glue between a [com.arttttt.nav3router.Router] and the result machinery.
 *
 * Created and attached to the router by [com.arttttt.nav3router.Nav3Host], it gives the result
 * extensions access to the durable [store], the live [backStack] (for best-effort cancellation
 * detection) and a [scope] on which active registrations observe the store.
 */
internal class ResultCoordinator(
    val store: ResultStore,
    val backStack: List<NavKey>,
    val scope: CoroutineScope,
)
