package com.arttttt.nav3router

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.arttttt.nav3router.result.popWithResult
import com.arttttt.nav3router.result.registerForResult
import com.arttttt.nav3router.test.RecordingNavigator
import com.arttttt.nav3router.test.bindForTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

private data object Home : NavKey
private data class Details(val id: String) : NavKey

class RouterTest {

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN a wired router WHEN pushing THEN the screen is on top`() = runTest {
        // GIVEN
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val router = Router<NavKey>()
        val backStack = router.bindForTest(stackOf(Home), scope = backgroundScope)

        // WHEN
        router.push(Details("x"))
        advanceUntilIdle()

        // THEN
        assertEquals(Details("x"), backStack.last())
    }

    @Test
    fun `GIVEN a multi-entry stack WHEN popping THEN the top is removed`() = runTest {
        // GIVEN
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val router = Router<NavKey>()
        val backStack = router.bindForTest(stackOf(Home, Details("x")), scope = backgroundScope)

        // WHEN
        router.pop()
        advanceUntilIdle()

        // THEN
        assertEquals(listOf(Home), backStack.toList())
    }

    @Test
    fun `GIVEN a result handler WHEN the producer pops with a result THEN it is delivered`() = runTest {
        // GIVEN
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val router = Router<NavKey>()
        router.bindForTest(stackOf(Home), scope = backgroundScope)
        var received: String? = null
        val registration = router.registerForResult<String> { received = it }

        // WHEN
        router.push(Details("x"))
        advanceUntilIdle()
        router.popWithResult("hello")
        advanceUntilIdle()

        // THEN
        assertEquals("hello", received)
        registration.dispose()
    }

    @Test
    fun `GIVEN a recording navigator WHEN pushing THEN the command is captured`() = runTest {
        // GIVEN
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val router = Router<NavKey>()
        val recorder = RecordingNavigator()
        router.bindForTest(recorder)

        // WHEN
        router.push(Details("x"))
        advanceUntilIdle()

        // THEN
        assertEquals(listOf<Command<NavKey>>(Push(Details("x"))), recorder.commands)
    }

    private fun stackOf(vararg elements: NavKey) =
        NavBackStack<NavKey>().apply { addAll(elements.asList()) }
}
