package com.arttttt.nav3router.test

/**
 * Marks the test-support wiring that reaches into Nav3Router's otherwise-internal machinery
 * (binding a [com.arttttt.nav3router.Router] to a navigator without a Compose `Nav3Host`).
 *
 * It ships in the main artifact because it needs internal access, but it is **test-only** — opt in
 * explicitly (or use the helpers in the `nav3-router-test` artifact, which opt in for you).
 */
@RequiresOptIn(
    level = RequiresOptIn.Level.WARNING,
    message = """Internal test-support API for Nav3Router — wires a Router without Nav3Host/Compose. Not for production navigation code.""",
)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class InternalNavTestApi
