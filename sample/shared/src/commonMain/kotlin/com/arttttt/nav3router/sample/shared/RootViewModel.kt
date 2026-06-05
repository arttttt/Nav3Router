package com.arttttt.nav3router.sample.shared

import androidx.navigation3.runtime.NavKey
import com.arttttt.nav3router.Router
import com.arttttt.nav3router.result.EphemeralResultApi
import com.arttttt.nav3router.result.pushForResult

/**
 * Holds the root screen's navigation logic, decoupled from Compose so it can be unit-tested with
 * `bindForTest` from the `nav3-router-test` artifact (see RootViewModelTest).
 */
class RootViewModel(
    private val router: Router<Screen>,
) {

    private var index = 0

    fun push() {
        router.push(Screen.Simple(++index))
    }

    fun pop() {
        router.pop()
        index--
    }

    fun replace() {
        router.replaceCurrent(Screen.Simple(++index))
    }

    fun pushChain() {
        router.push(
            Screen.Simple(++index),
            Screen.Simple(++index),
            Screen.Simple(++index),
        )
    }

    fun replaceStack() {
        router.replaceStack(Screen.Simple(++index))
    }

    fun clearStack() {
        router.clearStack()
    }

    fun dropStack() {
        router.dropStack()
    }

    fun showBottomSheet() {
        router.push(Screen.BottomSheet)
    }

    fun showDialog() {
        router.push(Screen.Dialog)
    }

    fun openNestedContainer() {
        router.push(Screen.NestedContainer())
    }

    @OptIn(EphemeralResultApi::class)
    fun pickColor(onPicked: (ColorResult) -> Unit) {
        router.pushForResult<ColorResult>(Screen.ColorPicker) { color ->
            onPicked(color)
        }
    }

    /** Keeps the Simple-screen counter in sync when system back pops a Simple screen. */
    fun onSystemBack(top: NavKey?) {
        if (top is Screen.Simple) index--
    }
}
