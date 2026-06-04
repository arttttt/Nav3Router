package com.arttttt.nav3router.result

/**
 * Handle to a result registration created by [registerForResult].
 *
 * Call [dispose] to stop receiving results — for example in `ViewModel.onCleared()` or in the
 * `onDispose` block of a `DisposableEffect`.
 */
fun interface ResultRegistration {

    fun dispose()
}
