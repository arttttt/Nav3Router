package com.arttttt.nav3router.result

/**
 * Marks the suspend/Flow result sugar ([openForResult], [resultFlow]).
 *
 * These convenience wrappers survive configuration change when invoked from a retained coroutine
 * scope (e.g. `viewModelScope`), but **not** process death: the suspended call / collector is gone
 * after the process is recreated. For full, process-death-durable delivery use the core
 * [registerForResult] instead.
 */
@RequiresOptIn(
    level = RequiresOptIn.Level.WARNING,
    message = """Ephemeral result API: survives configuration change in a retained scope, but NOT process death. Use Router.registerForResult for full durability.""",
)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION)
annotation class EphemeralResultApi
