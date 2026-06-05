package com.arttttt.nav3router.sample.shared

import com.arttttt.nav3router.Router
import com.arttttt.nav3router.result.popWithResult
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

/**
 * Example of how a consumer unit-tests navigation logic: [RootViewModel] takes a `Router`,
 * `bindForTest` wires it to a back stack without Compose, then we drive the view model and assert
 * the resulting stack (and result delivery) with plain assertions.
 */
class RootViewModelTest {

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN the root WHEN push THEN a Simple screen is added`() = runTest {
        // GIVEN
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val router = Router<Screen>()
        val backStack = router.bindForTest(scope = backgroundScope)
        val viewModel = RootViewModel(router)

        // WHEN
        viewModel.push()
        advanceUntilIdle()

        // THEN
        assertEquals(Screen.Simple(1), backStack.last())
    }

    @Test
    fun `GIVEN the root WHEN showBottomSheet THEN BottomSheet is opened`() = runTest {
        // GIVEN
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val router = Router<Screen>()
        val backStack = router.bindForTest(scope = backgroundScope)
        val viewModel = RootViewModel(router)

        // WHEN
        viewModel.showBottomSheet()
        advanceUntilIdle()

        // THEN
        assertEquals(Screen.BottomSheet, backStack.last())
    }

    @Test
    fun `GIVEN the root WHEN pushChain THEN three Simple screens are added`() = runTest {
        // GIVEN
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val router = Router<Screen>()
        val backStack = router.bindForTest(scope = backgroundScope)
        val viewModel = RootViewModel(router)

        // WHEN
        viewModel.pushChain()
        advanceUntilIdle()

        // THEN
        assertEquals(
            listOf(Screen.Simple(1), Screen.Simple(2), Screen.Simple(3)),
            backStack.toList(),
        )
    }

    @Test
    fun `GIVEN picking a color WHEN the picker returns THEN the result is delivered`() = runTest {
        // GIVEN
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val router = Router<Screen>()
        router.bindForTest(scope = backgroundScope)
        val viewModel = RootViewModel(router)
        var picked: ColorResult? = null

        // WHEN
        viewModel.pickColor { picked = it }
        advanceUntilIdle()
        router.popWithResult(ColorResult(argb = 0xFF112233L, name = "Test"))
        advanceUntilIdle()

        // THEN
        assertEquals(ColorResult(argb = 0xFF112233L, name = "Test"), picked)
    }
}
